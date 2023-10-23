package namdh.dhbkhn.datn.service;

import java.util.List;
import java.util.Optional;
import namdh.dhbkhn.datn.domain.Classroom;
import namdh.dhbkhn.datn.domain.ClassroomStatus;
import namdh.dhbkhn.datn.repository.ClassroomRepository;
import namdh.dhbkhn.datn.repository.ClassroomStatusRepository;
import namdh.dhbkhn.datn.service.dto.classroom.ClassroomInputDTO;
import namdh.dhbkhn.datn.service.dto.classroom.ClassroomOutputDTO;
import namdh.dhbkhn.datn.service.error.BadRequestException;
import namdh.dhbkhn.datn.service.utils.Utils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ClassroomService {

    private final ClassroomRepository classroomRepository;

    private final ClassroomStatusRepository classroomStatusRepository;

    public ClassroomService(ClassroomRepository classroomRepository, ClassroomStatusRepository classroomStatusRepository) {
        this.classroomRepository = classroomRepository;
        this.classroomStatusRepository = classroomStatusRepository;
    }

    public ClassroomOutputDTO create(ClassroomInputDTO classroomInputDTO) {
        Optional<Classroom> optional = classroomRepository.findByName(classroomInputDTO.getName());
        ClassroomOutputDTO classroomOutputDTO;
        if (!optional.isPresent()) {
            Classroom classroom = new Classroom();
            classroom.setName(classroomInputDTO.getName());
            classroomRepository.save(classroom);
            for (int i = 1; i < 54; i++) {
                ClassroomStatus classroomStatus = new ClassroomStatus();
                classroomStatus.setClassroom(classroom);
                classroomStatus.setWeek(i);
                classroomStatusRepository.save(classroomStatus);
            }
            classroomOutputDTO = new ClassroomOutputDTO(classroom);
        } else {
            classroomOutputDTO = new ClassroomOutputDTO(optional.get());
        }
        return classroomOutputDTO;
    }

    public ClassroomOutputDTO update(ClassroomInputDTO classroomInputDTO, long id) {
        ClassroomOutputDTO classroomOutputDTO;
        Classroom classroom = Utils.requireExists(classroomRepository.findById(id), "error.classroomNotFound");
        if (Utils.isAllSpaces(classroomInputDTO.getName()) || classroomInputDTO.getName().isEmpty()) {
            throw new BadRequestException("error.classroomNameEmptyOrBlank", null);
        }
        classroom.setName(classroomInputDTO.getName());
        classroomOutputDTO = new ClassroomOutputDTO(classroom);
        return classroomOutputDTO;
    }

    public Page<ClassroomOutputDTO> getAll(Pageable pageable, String name) {
        Page<ClassroomOutputDTO> page;
        if (name == null) {
            page = classroomRepository.findAllByNameIsNotNull(pageable).map(ClassroomOutputDTO::new);
        } else {
            page = classroomRepository.findAllByNameContainingIgnoreCase(pageable, name).map(ClassroomOutputDTO::new);
        }
        return page;
    }

    public ClassroomOutputDTO getOne(long id) {
        Classroom classroom = Utils.requireExists(classroomRepository.findById(id), "error.classroomNotFound");
        return new ClassroomOutputDTO(classroom);
    }

    public void delete(long id) {
        Classroom classroom = Utils.requireExists(classroomRepository.findById(id), "error.classroomNotFound");
        List<ClassroomStatus> classroomStatuses = classroomStatusRepository.findAllByClassroomId(classroom.getId());
        classroomStatusRepository.deleteAll(classroomStatuses);
        classroomRepository.delete(classroom);
    }
}
