package namdh.dhbkhn.datn.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import namdh.dhbkhn.datn.domain.ClassName;
import namdh.dhbkhn.datn.repository.ClassNameRepository;
import namdh.dhbkhn.datn.service.error.AccessForbiddenException;
import namdh.dhbkhn.datn.service.error.BadRequestException;
import namdh.dhbkhn.datn.service.utils.Utils;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ClassNameService {

    private final ClassNameRepository classNameRepository;

    private final UserACL userACL;

    public ClassNameService(ClassNameRepository classNameRepository, UserACL userACL) {
        this.classNameRepository = classNameRepository;
        this.userACL = userACL;
    }

    public List<ClassName> importClassList(InputStream inputStream) {
        if (!userACL.isUser()) {
            throw new AccessForbiddenException("error.notUser");
        }

        List<ClassName> classNameList = readExcelFileClassList(inputStream);

        if (classNameList.size() > 0) {
            for (ClassName className : classNameList) {
                Optional<ClassName> optional = classNameRepository.findByClassCode(className.getClassCode());
                if (!optional.isPresent()) {
                    classNameRepository.saveAndFlush(className);
                }
            }
        }
        return classNameList;
    }

    private List<ClassName> readExcelFileClassList(InputStream inputStream) {
        List<ClassName> result = new ArrayList<>();
        try (Workbook workbook = WorkbookFactory.create(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    // Ignore header
                    continue;
                }

                // Read cells and set value for book object
                ClassName className = new ClassName();
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
                                className.setSemester(Utils.handleWhitespace(cellValue.toString()));
                                break;
                            }
                        case 1:
                            {
                                if (cellValue == null) {
                                    className.setName(null);
                                    break;
                                }
                                className.setName(Utils.handleWhitespace(cellValue.toString()));
                                break;
                            }
                        case 2:
                            {
                                className.setClassCode(Integer.parseInt(Utils.handleWhitespace(cellValue.toString())));
                                break;
                            }
                        case 3:
                            {
                                if (cellValue == null) {
                                    className.setCourseCode(null);
                                    break;
                                }
                                className.setCourseCode(Utils.handleWhitespace(cellValue.toString()));
                                break;
                            }
                        case 4:
                            {
                                className.setStartWeek(Integer.parseInt(Utils.handleWhitespace(cellValue.toString())));
                                break;
                            }
                        case 5:
                            {
                                className.setNumberOfLessons(Integer.parseInt(Utils.handleWhitespace(cellValue.toString())));
                                break;
                            }
                        case 6:
                            {
                                if (cellValue == null) {
                                    className.setCondition(1);
                                    break;
                                }
                                className.setCondition(Integer.parseInt(Utils.handleWhitespace(cellValue.toString())));
                                break;
                            }
                    }
                }
                result.add(className);
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
