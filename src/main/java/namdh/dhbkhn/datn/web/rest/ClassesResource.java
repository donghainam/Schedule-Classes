package namdh.dhbkhn.datn.web.rest;

import java.io.IOException;
import namdh.dhbkhn.datn.service.ClassesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/classes")
public class ClassesResource {

    private final ClassesService classNameService;

    public ClassesResource(ClassesService classNameService) {
        this.classNameService = classNameService;
    }

    @PostMapping("/import")
    public void importClassViaExcel(@RequestParam("file") MultipartFile file) throws IOException {
        classNameService.importClassList(file.getInputStream());
    }
}
