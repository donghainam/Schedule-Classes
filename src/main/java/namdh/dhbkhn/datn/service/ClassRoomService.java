package namdh.dhbkhn.datn.service;

import java.util.Optional;
import namdh.dhbkhn.datn.domain.ClassRoom;
import namdh.dhbkhn.datn.repository.ClassRoomRepository;
import namdh.dhbkhn.datn.service.dto.class_room.ClassRoomInputDTO;
import namdh.dhbkhn.datn.service.dto.class_room.ClassRoomOutputDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ClassRoomService {

    private final ClassRoomRepository classRoomRepository;

    public ClassRoomService(ClassRoomRepository classRoomRepository) {
        this.classRoomRepository = classRoomRepository;
    }

    public ClassRoomOutputDTO create(ClassRoomInputDTO classRoomInputDTO) {
        Optional<ClassRoom> optional = classRoomRepository.findByName(classRoomInputDTO.getName());
        ClassRoomOutputDTO classRoomOutputDTO;
        if (!optional.isPresent()) {
            ClassRoom classRoom = new ClassRoom();
            classRoom.setName(classRoomInputDTO.getName());
            classRoomRepository.save(classRoom);
            classRoomOutputDTO = new ClassRoomOutputDTO(classRoom);
        } else {
            classRoomOutputDTO = new ClassRoomOutputDTO(optional.get());
        }
        return classRoomOutputDTO;
    }
}
