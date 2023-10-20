package namdh.dhbkhn.datn.service;

import java.util.ArrayList;
import java.util.List;
import namdh.dhbkhn.datn.domain.Classes;
import namdh.dhbkhn.datn.repository.ClassesRepository;
import namdh.dhbkhn.datn.repository.ClassroomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ScheduleService {

    private final ClassesRepository classesRepository;

    private final ClassroomRepository classRoomRepository;

    public ScheduleService(ClassesRepository classesRepository, ClassroomRepository classRoomRepository) {
        this.classesRepository = classesRepository;
        this.classRoomRepository = classRoomRepository;
    }

    public List<String> generateGreedySchedule(List<Classes> classes) {
        List<String> schedule = new ArrayList<>();

        // Get Classroom

        // TODO: Handle schedule
        for (int i = 1; i < 20; i++) {
            //anc
        }

        return schedule;
    }
}
