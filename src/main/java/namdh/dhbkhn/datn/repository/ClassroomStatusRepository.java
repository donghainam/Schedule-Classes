package namdh.dhbkhn.datn.repository;

import java.util.List;
import java.util.Optional;
import namdh.dhbkhn.datn.domain.Classroom;
import namdh.dhbkhn.datn.domain.ClassroomStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassroomStatusRepository extends JpaRepository<ClassroomStatus, Long> {
    @Query(
        value = "Select classroom_id from classroom_status where week = ?1 and status is false " + "order by id desc limit 0, 1",
        nativeQuery = true
    )
    Long getClassroomIdByStatus(int week);

    Optional<ClassroomStatus> findByClassroomAndWeek(Classroom classroom, int week);

    List<ClassroomStatus> findAllByClassroomId(long id);
}
