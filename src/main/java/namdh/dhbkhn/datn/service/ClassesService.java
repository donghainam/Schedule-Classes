package namdh.dhbkhn.datn.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import namdh.dhbkhn.datn.domain.Classes;
import namdh.dhbkhn.datn.repository.ClassesRepository;
import namdh.dhbkhn.datn.service.dto.class_name.ClassesInputDTO;
import namdh.dhbkhn.datn.service.dto.class_name.ClassesOutputDTO;
import namdh.dhbkhn.datn.service.error.BadRequestException;
import namdh.dhbkhn.datn.service.utils.Utils;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ClassesService {

    private static final Logger log = LoggerFactory.getLogger(ClassesService.class);

    private final ClassesRepository classesRepository;

    public ClassesService(ClassesRepository classesRepository) {
        this.classesRepository = classesRepository;
    }

    public void importClassList(InputStream inputStream) {
        List<ClassesOutputDTO> classNameList = readExcelFileClassList(inputStream);

        if (classNameList.size() > 0) {
            for (ClassesOutputDTO classesOutputDTO : classNameList) {
                Optional<Classes> optional = classesRepository.findByClassNote(classesOutputDTO.getClassNote());
                if (!optional.isPresent()) {
                    Classes className = new Classes(classesOutputDTO);
                    classesRepository.save(className);
                }
            }
        }
    }

    private List<ClassesOutputDTO> readExcelFileClassList(InputStream inputStream) {
        List<ClassesOutputDTO> result = new ArrayList<>();
        try (Workbook workbook = WorkbookFactory.create(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0 || row.getRowNum() == 1) {
                    // Ignore header
                    continue;
                }

                // Read cells and set value for classes object
                ClassesOutputDTO classesOutputDTO = new ClassesOutputDTO();
                for (int i = 0; i < 8; i++) {
                    Cell cell = row.getCell(i);
                    if (cell == null && i != 1 && i != 3 && i != 7) {
                        throw new BadRequestException("error.fieldNullOrEmpty", (row.getRowNum() + 1) + "-" + (i + 1));
                    }
                    Object cellValue = null;
                    if (!((i == 1 || i == 3 || i == 7) && cell == null)) {
                        cellValue = getCellValue(cell);
                    }

                    // Set value for classes object
                    switch (i) {
                        case 0:
                            classesOutputDTO.setSemester(Utils.handleDoubleNumber(cellValue.toString()));
                            break;
                        case 1:
                            if (cellValue == null) {
                                classesOutputDTO.setName(null);
                                break;
                            }
                            classesOutputDTO.setName(Utils.handleWhitespace(cellValue.toString()));
                            break;
                        case 2:
                            classesOutputDTO.setClassNote(Utils.handleWhitespace(cellValue.toString()));
                            break;
                        case 3:
                            if (cellValue == null) {
                                classesOutputDTO.setCourseCode(null);
                                break;
                            }
                            classesOutputDTO.setCourseCode(Utils.handleWhitespace(cellValue.toString()));
                            break;
                        case 4:
                            classesOutputDTO.setStartWeek(Integer.parseInt(Utils.handleDoubleNumber(cellValue.toString())));
                            break;
                        case 5:
                            classesOutputDTO.setNumberOfLessons(Integer.parseInt(Utils.handleDoubleNumber(cellValue.toString())));
                            break;
                        case 6:
                            classesOutputDTO.setNumberOfWeekStudy(
                                Integer.parseInt(Utils.handleDoubleNumber(cellValue.toString())) / classesOutputDTO.getNumberOfLessons()
                            );
                            break;
                        case 7:
                            if (cellValue == null) {
                                classesOutputDTO.setConditions(1);
                                break;
                            }
                            classesOutputDTO.setConditions(Integer.parseInt(Utils.handleDoubleNumber(cellValue.toString())));
                            break;
                    }
                }
                result.add(classesOutputDTO);
            }

            workbook.close();
            inputStream.close();

            return result;
        } catch (IOException e) {
            log.error("Error upload excel file wrong format", e);
            throw new BadRequestException("error.uploadExcelWrongFormat", null);
        }
    }

    private static Object getCellValue(Cell cell) {
        CellType cellType = cell.getCellType();
        Object cellValue = null;
        switch (cellType) {
            case BOOLEAN:
                cellValue = cell.getBooleanCellValue();
                break;
            case FORMULA:
                Workbook workbook = cell.getSheet().getWorkbook();
                FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
                cellValue = evaluator.evaluate(cell).getNumberValue();
                break;
            case NUMERIC:
                cellValue = cell.getNumericCellValue();
                break;
            case STRING:
                cellValue = cell.getStringCellValue();
                break;
            case _NONE:
            case BLANK:
                if (cell.getColumnIndex() == 1 || cell.getColumnIndex() == 3 || cell.getColumnIndex() == 7) {
                    break;
                }
            case ERROR:
                throw new BadRequestException("error.cellContentError", (cell.getRowIndex() + 1) + "-" + (cell.getColumnIndex() + 1));
            default:
                break;
        }
        return cellValue;
    }

    public ClassesOutputDTO update(ClassesInputDTO classesInputDTO, long id) {
        ClassesOutputDTO classesOutputDTO;
        Classes classes = Utils.requireExists(classesRepository.findById(id), "error.classesNotFound");
        String className = classesInputDTO.getName();
        if (Utils.isAllSpaces(className) || className.isEmpty()) {
            throw new BadRequestException("error.classNameEmptyOrBlank", null);
        }
        classes.setName(className);
        String classNote = classesInputDTO.getClassNote();
        if (Utils.isAllSpaces(classNote) || classNote.isEmpty()) {
            throw new BadRequestException("error.classNoteEmptyOrBlank", null);
        }
        classes.setClassNote(classNote);
        String courseCode = classesInputDTO.getCourseCode();
        if (Utils.isAllSpaces(courseCode) || courseCode.isEmpty()) {
            throw new BadRequestException("error.courseCodeEmptyOrBlank", null);
        }
        classes.setCourseCode(courseCode);
        int startWeek = classesInputDTO.getStartWeek();
        if (startWeek < 1 || startWeek > 53) {
            throw new BadRequestException("error.startWeekIncorrect", null);
        }
        classes.setStartWeek(startWeek);
        int numberOfLessons = classesInputDTO.getNumberOfLessons();
        if (numberOfLessons < 1 || numberOfLessons > 6) {
            throw new BadRequestException("error.numberOfLessonsIncorrect", null);
        }
        classes.setNumberOfLessons(numberOfLessons);
        int numberOfWeekStudy = classesInputDTO.getNumberOfWeekStudy();
        if (numberOfWeekStudy < 1 || numberOfWeekStudy > 21) {
            throw new BadRequestException("error.startWeekIncorrect", null);
        }
        classes.setNumberOfWeekStudy(numberOfWeekStudy);
        String semester = classesInputDTO.getSemester();
        if (Utils.isAllSpaces(semester) || semester.isEmpty()) {
            throw new BadRequestException("error.semesterEmptyOrBlank", null);
        }
        classes.setSemester(semester);
        int conditions = classesInputDTO.getConditions();
        if (conditions < 1) {
            throw new BadRequestException("error.conditionsIncorrect", null);
        }
        classes.setConditions(conditions);
        classesOutputDTO = new ClassesOutputDTO(classes);
        return classesOutputDTO;
    }

    public Page<ClassesOutputDTO> getAll(Pageable pageable, String name) {
        Page<ClassesOutputDTO> page;
        if (name == null) {
            page = classesRepository.findAllByNameIsNotNull(pageable).map(ClassesOutputDTO::new);
        } else {
            page = classesRepository.findAllByNameContainingIgnoreCase(pageable, name).map(ClassesOutputDTO::new);
        }
        return page;
    }

    public ClassesOutputDTO getOne(long id) {
        Classes classes = Utils.requireExists(classesRepository.findById(id), "error.classesNotFound");
        return new ClassesOutputDTO(classes);
    }

    public void delete(long id) {
        Classes classes = Utils.requireExists(classesRepository.findById(id), "error.classesNotFound");
        classesRepository.delete(classes);
    }
}
