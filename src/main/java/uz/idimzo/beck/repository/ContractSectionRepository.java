package uz.idimzo.beck.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.idimzo.beck.entity.ContractSection;

import java.util.List;

@Repository
public interface ContractSectionRepository extends JpaRepository<ContractSection, Long> {
    List<ContractSection> findByTranslationId(Long translationId);
}