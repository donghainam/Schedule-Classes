package namdh.dhbkhn.datn.web.rest;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.io.IOException;
import namdh.dhbkhn.datn.service.ClassesService;
import namdh.dhbkhn.datn.service.dto.class_name.ClassesInputDTO;
import namdh.dhbkhn.datn.service.dto.class_name.ClassesOutputDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/class-name")
public class ClassesResource {

    private final ClassesService classNameService;

    public ClassesResource(ClassesService classNameService) {
        this.classNameService = classNameService;
    }

    @ApiOperation(value = "Create new class", notes = "This endpoint to create new class via Excel file")
    @ApiResponses(
        {
            @ApiResponse(code = 200, message = "Ok - import successfully"),
            @ApiResponse(code = 400, message = "Bad Request - Upload excel file failed or excel validation caught"),
            @ApiResponse(
                code = 500,
                message = "Internal Error - There is error during process, you should try again and contact with developer in this case"
            ),
        }
    )
    @PostMapping("/import")
    public void importClassViaExcel(@RequestParam("file") MultipartFile file) throws IOException {
        classNameService.importClassList(file.getInputStream());
    }
}
