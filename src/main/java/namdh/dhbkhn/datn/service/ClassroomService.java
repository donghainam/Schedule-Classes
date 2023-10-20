package namdh.dhbkhn.datn.service;

import java.util.Optional;
import namdh.dhbkhn.datn.domain.Classroom;
import namdh.dhbkhn.datn.repository.ClassroomRepository;
import namdh.dhbkhn.datn.service.dto.class_room.ClassRoomInputDTO;
import namdh.dhbkhn.datn.service.dto.class_room.ClassRoomOutputDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ClassroomService {

    private final ClassroomRepository classroomRepository;

    public ClassroomService(ClassroomRepository classRoomRepository) {
        this.classroomRepository = classRoomRepository;
    }

    public ClassRoomOutputDTO create(ClassRoomInputDTO classRoomInputDTO) {
        Optional<Classroom> optional = classroomRepository.findByName(classRoomInputDTO.getName());
        ClassRoomOutputDTO classRoomOutputDTO;
        if (!optional.isPresent()) {
            Classroom classRoom = new Classroom();
            classRoom.setName(classRoomInputDTO.getName());
            classroomRepository.save(classRoom);
            classRoomOutputDTO = new ClassRoomOutputDTO(classRoom);
        } else {
            classRoomOutputDTO = new ClassRoomOutputDTO(optional.get());
        }
        return classRoomOutputDTO;
    }
}
