package namdh.dhbkhn.datn.repository;

import java.util.List;
import java.util.Optional;
import namdh.dhbkhn.datn.domain.Classes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassesRepository extends JpaRepository<Classes, Long> {
    Optional<Classes> findByClassNote(String classNote);

    @Query(
        value = "Select * from classes where start_week <= ?1 " +
        "and count_condition = 0 and count_week_studied < 15 / number_of_lessons " +
        "and upper(semester) like upper(concat('%', ?2, '%')) order by id desc",
        nativeQuery = true
    )
    List<Classes> getAllClasses(int startWeek, String semester);

    List<Classes> getClassesBySemester(String semester);
}
