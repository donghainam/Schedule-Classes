package namdh.dhbkhn.datn.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import namdh.dhbkhn.datn.domain.Classes;
import namdh.dhbkhn.datn.domain.Classroom;
import namdh.dhbkhn.datn.domain.ClassroomStatus;
import namdh.dhbkhn.datn.repository.ClassesRepository;
import namdh.dhbkhn.datn.repository.ClassroomRepository;
import namdh.dhbkhn.datn.repository.ClassroomStatusRepository;
import namdh.dhbkhn.datn.service.dto.schedule.ScheduleDTO;
import namdh.dhbkhn.datn.service.utils.Utils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ScheduleService {

    private static final Logger log = LoggerFactory.getLogger(ScheduleService.class);

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

    public byte[] exportSchedule(String semester) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Schedule");

            CellStyle style = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setFontHeightInPoints((short) 13);
            style.setFont(font);

            Row rowHead = sheet.createRow(0);
            rowHead.createCell(0).setCellValue("CourseCode");
            rowHead.createCell(1).setCellValue("CourseName");
            rowHead.createCell(2).setCellValue("Note");
            rowHead.createCell(3).setCellValue("WeekNote");
            rowHead.createCell(4).setCellValue("WeekDay");
            rowHead.createCell(5).setCellValue("Begin");
            rowHead.createCell(6).setCellValue("End");
            rowHead.createCell(7).setCellValue("Room");

            for (int j = 0; j < 8; j++) {
                rowHead.getCell(j).setCellStyle(style);
            }

            List<ScheduleDTO> scheduleDTOS = this.getSchedule(semester);
            this.writeToSheet(sheet, scheduleDTOS, 1);

            workbook.write(output);
            return output.toByteArray();
        } catch (IOException e) {
            log.error("Error when create Excel file: ", e);
        }
        return new byte[0];
    }

    public List<ScheduleDTO> getSchedule(String semester) {
        List<ScheduleDTO> result = new ArrayList<>();

        // Handle old data
        List<ClassroomStatus> classroomStatuses = classroomStatusRepository.findAll();
        for (ClassroomStatus classroomStatus : classroomStatuses) {
            classroomStatus.setTimeNote(null);
            classroomStatus.setStatus(0);
        }
        this.classroomStatusRepository.saveAll(classroomStatuses);
        List<Classes> clList = classesRepository.getClassesBySemester(semester);
        for (Classes classes : clList) {
            classes.setCountWeekStudied(0);
        }
        this.classesRepository.saveAll(clList);

        // Get start, end week for semester
        String st = semester.substring(4);
        int start;
        int end;
        if (st.equals("1")) {
            start = 1;
            end = 21;
        } else {
            start = 25;
            end = 45;
        }
        for (int i = start; i < end; i++) {
            if (i == 11 || i == 35) {
                // Skip week 11, 35
                continue;
            }
            // Get all Classes
            List<Classes> classesList = classesRepository.getAllClasses(i, semester);
            // Sort by priority
            this.sortByPriority(classesList);

            for (Classes classes : classesList) {
                int status;
                if (classes.getNumberOfLessons() > 3) {
                    status = 0;
                } else {
                    status = 1;
                }
                // Get one classroom from db
                ClassroomStatus classroomStatus = classroomStatusRepository.getClassroomIdByStatus(i, status);
                if (classroomStatus == null) {
                    log.info("Classrooms aren't enough for all classes");
                }
                Classroom classroom = classroomStatus.getClassroom();
                int[][] w = new int[5][2];
                if (classroomStatus.getTimeNote() != null) {
                    this.initArray(w);
                    this.getTimeNote(w, classroomStatus);
                } else {
                    this.initArray(w);
                }
                if (checkClassroom(w, classes.getNumberOfLessons())) {
                    this.scheduleClasses(classes, classroom, w, result, i);
                } else {
                    log.info("Classrooms aren't enough for all classes");
                }
            }
        }
        return result;
    }

    private void scheduleClasses(Classes classes, Classroom classroom, int[][] w, List<ScheduleDTO> list, int week) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 2; j++) {
                if (w[i][j] >= classes.getNumberOfLessons()) {
                    String begin = this.getBegin(w[i][j] + 10 * j);
                    String end = this.getEnd(w[i][j] + 10 * j, classes.getNumberOfLessons());
                    List<Integer> weekNote = this.scheduleTheClassWeek(classes.getId(), classroom.getId(), week, end, i, w[i][j]);

                    ScheduleDTO scheduleDTO = new ScheduleDTO();
                    scheduleDTO.setCourseCode(classes.getCourseCode());
                    scheduleDTO.setClassName(classes.getName());
                    scheduleDTO.setClassNote(classes.getClassNote());
                    scheduleDTO.setClassroom(classroom.getName());

                    // TimeNote
                    scheduleDTO.addTimeNote("WeekNote", weekNote);
                    scheduleDTO.addTimeNote("WeekDay", i + 2);
                    scheduleDTO.addTimeNote("Begin", begin);
                    scheduleDTO.addTimeNote("End", end);
                    list.add(scheduleDTO);
                    w[i][j] -= classes.getNumberOfLessons();
                    return;
                }
            }
        }
    }

    private List<Integer> scheduleTheClassWeek(
        long classesId,
        long classroomId,
        int weekFirst,
        String end,
        int weekDay,
        int remainingLessons
    ) {
        List<Integer> result = new ArrayList<>();
        Classes classes = Utils.requireExists(classesRepository.findById(classesId), "error.classesNotFound");
        int cnt = classes.getCountWeekStudied();
        int lastWeek;
        if (weekFirst > 20) {
            lastWeek = 51;
        } else {
            lastWeek = 23;
        }
        int session = this.getSession(end);
        while (cnt < classes.getNumberOfWeekStudy() && weekFirst < lastWeek) {
            if (weekFirst == 11 || weekFirst == 35) {
                weekFirst++;
                continue;
            }
            ClassroomStatus classroomStatus = Utils.requireExists(
                classroomStatusRepository.findByClassroomIdAndWeek(classroomId, weekFirst),
                "error.classroomWeekNotFound"
            );
            // Get data of classroom
            int[][] w = new int[5][2];
            if (classroomStatus.getTimeNote() != null) {
                this.initArray(w);
                this.getTimeNote(w, classroomStatus);
            } else {
                this.initArray(w);
            }

            // Check week and save
            if (w[weekDay][session] == remainingLessons) {
                result.add(weekFirst);
                List<String> stringList = classroomStatus.getTimeNoteExtra(String.valueOf(weekDay));
                if (stringList == null) {
                    stringList = new ArrayList<>();
                }
                stringList.add(end);
                classroomStatus.addTimeNote(String.valueOf(weekDay), stringList);
                // Handle condition classroom
                w[weekDay][session] -= classes.getNumberOfLessons();
                if (!checkClassroom(w, 3)) {
                    classroomStatus.setStatus(2);
                } else if (!checkClassroom(w, 5)) {
                    classroomStatus.setStatus(1);
                }
                classroomStatusRepository.save(classroomStatus);
                weekFirst += classes.getConditions();
            } else {
                weekFirst++;
            }
            cnt++;
        }
        classes.setCountWeekStudied(cnt);
        classesRepository.save(classes);
        return result;
    }

    public void writeToSheet(Sheet sheet, List<ScheduleDTO> scheduleDTOS, int line) {
        for (ScheduleDTO scheduleDTO : scheduleDTOS) {
            Row row = sheet.createRow(line++);
            row.createCell(0).setCellValue(scheduleDTO.getCourseCode());
            row.createCell(1).setCellValue(scheduleDTO.getClassName());
            row.createCell(2).setCellValue(scheduleDTO.getClassNote());
            List<Integer> weekNote = scheduleDTO.getTimeNoteExtra("WeekNote");
            String weekNoteSt = weekNote.stream().map(String::valueOf).collect(Collectors.joining(","));
            row.createCell(3).setCellValue(weekNoteSt);
            int weekDay = scheduleDTO.getTimeNoteExtra("WeekDay");
            row.createCell(4).setCellValue(String.valueOf(weekDay));
            row.createCell(5).setCellValue((String) scheduleDTO.getTimeNoteExtra("Begin"));
            row.createCell(6).setCellValue((String) scheduleDTO.getTimeNoteExtra("End"));
            row.createCell(7).setCellValue(scheduleDTO.getClassroom());
        }
    }

    private void sortByPriority(List<Classes> input) {
        input.sort((cls1, cls2) -> {
            // Sort by startWeek in ascending order
            int startWeekComparison = Integer.compare(cls1.getStartWeek(), cls2.getStartWeek());
            // If startWeeks are equal, sort by numberOfWeekStudy in descending order
            if (startWeekComparison == 0) {
                int numberOfWeekStudyComparison = Integer.compare(cls2.getNumberOfWeekStudy(), cls1.getNumberOfWeekStudy());
                // If startWeeks are equal, sort by numberOfLesson in descending order
                if (numberOfWeekStudyComparison == 0) {
                    return Integer.compare(cls2.getNumberOfLessons(), cls1.getNumberOfLessons());
                }
                return numberOfWeekStudyComparison;
            }
            return startWeekComparison;
        });
    }

    private void getTimeNote(int[][] w, ClassroomStatus classroomStatus) {
        for (int i = 0; i < 5; i++) {
            List<String> ends = classroomStatus.getTimeNoteExtra(String.valueOf(i));
            if (ends != null) {
                for (String end : ends) {
                    int session = this.getSession(end);
                    w[i][session] = this.getRemainingWOfSession(end);
                }
            }
        }
    }

    private void initArray(int[][] w) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 2; j++) {
                w[i][j] = 6;
            }
        }
    }

    private boolean checkClassroom(int[][] w, int numberOfLesson) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 2; j++) {
                if (w[i][j] >= numberOfLesson) {
                    return true;
                }
            }
        }
        return false;
    }

    private String getBegin(int w) {
        String result;
        switch (w) {
            case 6:
                result = "0645";
                break;
            case 5:
                result = "0730";
                break;
            case 4:
                result = "0825";
                break;
            case 3:
                result = "0920";
                break;
            case 2:
                result = "1015";
                break;
            case 1:
                result = "1100";
                break;
            case 16:
                result = "1230";
                break;
            case 15:
                result = "1315";
                break;
            case 14:
                result = "1410";
                break;
            case 13:
                result = "1505";
                break;
            case 12:
                result = "1600";
                break;
            default:
                result = "1645";
        }
        return result;
    }

    private String getEnd(int w, int numberOfLesson) {
        String result;
        switch (w - numberOfLesson) {
            case 0:
                result = "1145";
                break;
            case 1:
                result = "1100";
                break;
            case 2:
                result = "1005";
                break;
            case 3:
                result = "0910";
                break;
            case 4:
                result = "0815";
                break;
            case 5:
                result = "0730";
                break;
            case 10:
                result = "1730";
                break;
            case 11:
                result = "1645";
                break;
            case 12:
                result = "1550";
                break;
            case 13:
                result = "1455";
                break;
            case 14:
                result = "1400";
                break;
            default:
                result = "1315";
        }
        return result;
    }

    private int getSession(String end) {
        int session;
        switch (end) {
            case "0730":
            case "0815":
            case "0910":
            case "1005":
            case "1100":
            case "1145":
                session = 0;
                break;
            default:
                session = 1;
        }
        return session;
    }

    private int getRemainingWOfSession(String end) {
        int w;
        switch (end) {
            case "1145":
            case "1730":
                w = 0;
                break;
            case "1100":
            case "1645":
                w = 1;
                break;
            case "1005":
            case "1550":
                w = 2;
                break;
            case "0910":
            case "1455":
                w = 3;
                break;
            case "0815":
            case "1400":
                w = 4;
                break;
            case "0730":
            case "1315":
                w = 5;
                break;
            default:
                w = 6;
        }
        return w;
    }
}
