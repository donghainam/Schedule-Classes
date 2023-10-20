package namdh.dhbkhn.datn.web.rest;

import namdh.dhbkhn.datn.service.ClassroomService;
import namdh.dhbkhn.datn.service.dto.class_room.ClassRoomInputDTO;
import namdh.dhbkhn.datn.service.dto.class_room.ClassRoomOutputDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/class-room")
public class ClassroomResource {

    private final ClassroomService classroomService;

    public ClassroomResource(ClassroomService classRoomService) {
        this.classroomService = classRoomService;
    }

    @PostMapping("/create")
    public ResponseEntity<ClassRoomOutputDTO> create(@RequestBody ClassRoomInputDTO classRoomInputDTO) {
        return new ResponseEntity<>(this.classroomService.create(classRoomInputDTO), HttpStatus.CREATED);
    }
}
