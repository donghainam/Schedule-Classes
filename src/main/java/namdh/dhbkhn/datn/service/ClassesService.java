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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ClassesService {

    private final ClassesRepository classesRepository;

    public ClassesService(ClassesRepository classesRepository) {
        this.classesRepository = classesRepository;
    }

    public ClassesOutputDTO create(ClassesInputDTO classesInputDTO) {
        ClassesOutputDTO classNameOutputDTO;
        Classes classes = new Classes();
        classes.setName(classesInputDTO.getName());
        classes.setClassCode(classesInputDTO.getClassCode());
        classes.setCourseCode(classesInputDTO.getCourseCode());
        classes.setStartWeek(classesInputDTO.getStartWeek());
        classes.setNumberOfLessons(classesInputDTO.getNumberOfLessons());
        classes.setSemester(classesInputDTO.getSemester());
        classes.setCondition(classesInputDTO.getCondition());
        classes.setCountCondition(0);
        classes.setCountWeekStudied(0);
        classesRepository.save(classes);
        classNameOutputDTO = new ClassesOutputDTO(classes);
        return classNameOutputDTO;
    }

    public List<Classes> importClassList(InputStream inputStream) {
        List<ClassesOutputDTO> classNameList = readExcelFileClassList(inputStream);
        List<Classes> result = new ArrayList<>();

        if (classNameList.size() > 0) {
            for (ClassesOutputDTO classesOutputDTO : classNameList) {
                Optional<Classes> optional = classesRepository.findByClassCode(classesOutputDTO.getClassCode());
                if (!optional.isPresent()) {
                    Classes className = new Classes(classesOutputDTO);
                    classesRepository.save(className);
                    result.add(className);
                }
            }
        }
        return result;
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

                // Read cells and set value for book object
                ClassesOutputDTO classNameDTO = new ClassesOutputDTO();
                for (int i = 0; i < 7; i++) {
                    Cell cell = row.getCell(i);
                    if (cell == null && i != 1 && i != 3 && i != 6) {
                        throw new BadRequestException("error.fieldNullOrEmpty", (row.getRowNum() + 1) + "-" + (i + 1));
                    }
                    Object cellValue = null;
                    if (!((i == 1 || i == 3 || i == 6) && cell == null)) {
                        cellValue = getCellValue(cell);
                    }

                    // Set value for book object
                    switch (i) {
                        case 0:
                            {
                                classNameDTO.setSemester(Utils.handleDoubleNumber(cellValue.toString()));
                                break;
                            }
                        case 1:
                            {
                                if (cellValue == null) {
                                    classNameDTO.setName(null);
                                    break;
                                }
                                classNameDTO.setName(Utils.handleWhitespace(cellValue.toString()));
                                break;
                            }
                        case 2:
                            {
                                classNameDTO.setClassCode(Integer.parseInt(Utils.handleDoubleNumber(cellValue.toString())));
                                break;
                            }
                        case 3:
                            {
                                if (cellValue == null) {
                                    classNameDTO.setCourseCode(null);
                                    break;
                                }
                                classNameDTO.setCourseCode(Utils.handleWhitespace(cellValue.toString()));
                                break;
                            }
                        case 4:
                            {
                                classNameDTO.setStartWeek(Integer.parseInt(Utils.handleDoubleNumber(cellValue.toString())));
                                break;
                            }
                        case 5:
                            {
                                classNameDTO.setNumberOfLessons(Integer.parseInt(Utils.handleDoubleNumber(cellValue.toString())));
                                break;
                            }
                        case 6:
                            {
                                if (cellValue == null) {
                                    classNameDTO.setCondition(1);
                                    break;
                                }
                                classNameDTO.setCondition(Integer.parseInt(Utils.handleDoubleNumber(cellValue.toString())));
                                break;
                            }
                    }
                }
                result.add(classNameDTO);
            }

            workbook.close();
            inputStream.close();

            return result;
        } catch (IOException e) {
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
                if (cell.getColumnIndex() == 1 || cell.getColumnIndex() == 3 || cell.getColumnIndex() == 6) {
                    break;
                }
            case ERROR:
                throw new BadRequestException("error.cellContentError", (cell.getRowIndex() + 1) + "-" + (cell.getColumnIndex() + 1));
            default:
                break;
        }
        return cellValue;
    }
}
