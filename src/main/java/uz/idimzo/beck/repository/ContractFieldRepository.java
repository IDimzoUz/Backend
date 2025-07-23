package uz.idimzo.beck.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.idimzo.beck.entity.ContractField;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContractFieldRepository extends JpaRepository<ContractField, Long> {
    List<ContractField> findBySectionId(Long sectionId);
    Optional<ContractField> findByFieldId(String fieldId);
}