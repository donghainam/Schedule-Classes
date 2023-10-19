package namdh.dhbkhn.datn.repository;

import java.util.Optional;
import namdh.dhbkhn.datn.domain.ClassName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassNameRepository extends JpaRepository<ClassName, Long> {
    Optional<ClassName> findByClassCode(int classCode);
}
