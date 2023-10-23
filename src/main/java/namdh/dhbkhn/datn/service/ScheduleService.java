package namdh.dhbkhn.datn.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import namdh.dhbkhn.datn.domain.Classes;
import namdh.dhbkhn.datn.domain.Classroom;
import namdh.dhbkhn.datn.domain.ClassroomStatus;
import namdh.dhbkhn.datn.domain.enumeration.WeekDay;
import namdh.dhbkhn.datn.repository.ClassesRepository;
import namdh.dhbkhn.datn.repository.ClassroomRepository;
import namdh.dhbkhn.datn.repository.ClassroomStatusRepository;
import namdh.dhbkhn.datn.service.dto.schedule.ScheduleDTO;
import namdh.dhbkhn.datn.service.error.BadRequestException;
import namdh.dhbkhn.datn.service.utils.Utils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ScheduleService {

    private final ClassesRepository classesRepository;

    private final ClassroomRepository classroomRepository;

    private final ClassroomStatusRepository classroomStatusRepository;

    public ScheduleService(
        ClassesRepository classesRepository,
        ClassroomRepository classRoomRepository,
        ClassroomStatusRepository classroomStatusRepository
    ) {
        this.classesRepository = classesRepository;
        this.classroomRepository = classRoomRepository;
        this.classroomStatusRepository = classroomStatusRepository;
    }

    public byte[] exportSchedule() {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Schedule");

            CellStyle style = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setFontHeightInPoints((short) 13);
            style.setFont(font);

            Row rowHead = sheet.createRow(0);
            rowHead.createCell(0).setCellValue("Phòng");
            rowHead.createCell(1).setCellValue("Tuần");
            rowHead.createCell(2).setCellValue("Thứ");
            rowHead.createCell(3).setCellValue("Buổi");
            rowHead.createCell(4).setCellValue("Tên_HP");

            for (int j = 0; j < 5; j++) {
                rowHead.getCell(j).setCellStyle(style);
            }

            List<ScheduleDTO> scheduleDTOS = this.generateGreedySchedule();
            this.writeToSheet(sheet, scheduleDTOS, 1);

            workbook.write(output);
            return output.toByteArray();
        } catch (IOException e) {
            //            log.error("Error when create Excel file: ", e);
            //            throw new BadRequestException("error.errorWhileExport" + e, null);
        }
        return new byte[0];
    }

    public List<ScheduleDTO> generateGreedySchedule() {
        List<ScheduleDTO> result = new ArrayList<>();

        // Handle old data

        for (int i = 1; i < 20; i++) {
            // Get one classroom from db
            Long classroomId = classroomStatusRepository.getClassroomIdByStatus(i);
            if (classroomId == null) {
                throw new BadRequestException("error.notFoundClassroom", null);
            }
            Classroom classroom = Utils.requireExists(classroomRepository.findById(classroomId), "error.notExisted");
            int cnt = 0;
            // Get all Classes
            List<Classes> classesList = classesRepository.getAllClasses(i, "20231");
            int[][] w = new int[5][2];
            this.initArray(w, 5, 2);
            // Sort by priority
            this.sortByPriority(classesList);
            for (Classes classes : classesList) {
                // Schedule classes during weekdays

                if (this.schedule(classes, w, result, i, classroom.getName())) {
                    // Handle condition of classes and save week study
                    classes.setCountCondition(classes.getConditions() - 1);
                    classes.setCountWeekStudied(classes.getCountWeekStudied() + 1);
                    classesRepository.save(classes);
                    cnt++;
                } else if (classesList.size() > cnt) {
                    ClassroomStatus classroomStatus = Utils.requireExists(
                        classroomStatusRepository.findByClassroomAndWeek(classroom, i),
                        "error.notFound"
                    );
                    classroomStatus.setStatus(true);
                    classroomStatusRepository.save(classroomStatus);
                    Long anotherClassroomId = classroomStatusRepository.getClassroomIdByStatus(i);
                    if (anotherClassroomId == null) {
                        break;
                    }
                    classroom = Utils.requireExists(classroomRepository.findById(anotherClassroomId), "error.notExisted");
                    this.initArray(w, 5, 2);
                    if (this.schedule(classes, w, result, i, classroom.getName())) {
                        classes.setCountCondition(classes.getConditions() - 1);
                        classes.setCountWeekStudied(classes.getCountWeekStudied() + 1);
                        classesRepository.save(classes);
                        cnt++;
                    }
                }
            }
        }
        return result;
    }

    public Sheet writeToSheet(Sheet sheet, List<ScheduleDTO> scheduleDTOS, int line) {
        for (ScheduleDTO scheduleDTO : scheduleDTOS) {
            Row row = sheet.createRow(line++);
            row.createCell(0).setCellValue(scheduleDTO.getClassroom());
            row.createCell(1).setCellValue(scheduleDTO.getWeek());
            row.createCell(2).setCellValue(scheduleDTO.getDay().toString());
            row.createCell(3).setCellValue(scheduleDTO.getSession());
            row.createCell(4).setCellValue(scheduleDTO.getClassName());
        }
        return sheet;
    }

    private void sortByPriority(List<Classes> input) {
        input.sort((cls1, cls2) -> {
            // Sort by startWeek in ascending order
            int startWeekComparison = Integer.compare(cls1.getStartWeek(), cls2.getStartWeek());
            // If startWeeks are equal, sort by numberOfLesson in descending order
            if (startWeekComparison == 0) {
                return Integer.compare(cls2.getNumberOfLessons(), cls1.getNumberOfLessons());
            }
            return startWeekComparison;
        });
    }

    private void initArray(int[][] w, int n, int m) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                w[i][j] = 6;
            }
        }
    }

    private boolean schedule(Classes classes, int[][] w, List<ScheduleDTO> list, int week, String classroom) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 2; j++) {
                if (w[i][j] >= classes.getNumberOfLessons()) {
                    w[i][j] -= classes.getNumberOfLessons();
                    ScheduleDTO scheduleDTO = new ScheduleDTO();
                    scheduleDTO.setClassroom(classroom);
                    scheduleDTO.setWeek(week);
                    switch (i) {
                        case 0:
                            scheduleDTO.setDay(WeekDay.MON);
                            break;
                        case 1:
                            scheduleDTO.setDay(WeekDay.TUE);
                            break;
                        case 2:
                            scheduleDTO.setDay(WeekDay.WED);
                            break;
                        case 3:
                            scheduleDTO.setDay(WeekDay.THU);
                            break;
                        case 4:
                            scheduleDTO.setDay(WeekDay.FRI);
                            break;
                    }
                    switch (j) {
                        case 0:
                            scheduleDTO.setSession("Morning");
                            break;
                        case 1:
                            scheduleDTO.setSession("Afternoon");
                            break;
                    }
                    scheduleDTO.setClassName(classes.getName());
                    list.add(scheduleDTO);
                    return true;
                }
            }
        }
        return false;
    }
}
