package namdh.dhbkhn.datn.repository;

import java.util.Optional;
import namdh.dhbkhn.datn.domain.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassroomRepository extends JpaRepository<Classroom, Long> {
    Optional<Classroom> findByName(String name);
}
