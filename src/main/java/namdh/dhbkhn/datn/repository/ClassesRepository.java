package namdh.dhbkhn.datn.repository;

import java.util.Optional;
import namdh.dhbkhn.datn.domain.Classes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassesRepository extends JpaRepository<Classes, Long> {
    Optional<Classes> findByClassCode(int classCode);
}
