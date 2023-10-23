package namdh.dhbkhn.datn.repository;

import java.util.Optional;
import namdh.dhbkhn.datn.domain.Classroom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassroomRepository extends JpaRepository<Classroom, Long> {
    Optional<Classroom> findByName(String name);

    @Query(value = "Select * from classroom where status is false order by id desc limit 0, 1", nativeQuery = true)
    Classroom getLastClassroomByStatusIsFalse();

    Page<Classroom> findAllByNameIsNotNull(Pageable pageable);

    Page<Classroom> findAllByNameContainingIgnoreCase(Pageable pageable, String name);
}
