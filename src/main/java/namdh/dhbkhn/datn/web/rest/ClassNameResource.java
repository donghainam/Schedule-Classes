package namdh.dhbkhn.datn.web.rest;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.io.IOException;
import namdh.dhbkhn.datn.service.ClassNameService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/class-name")
public class ClassNameResource {

    private final ClassNameService classNameService;

    public ClassNameResource(ClassNameService classNameService) {
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
