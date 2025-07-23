package uz.idimzo.beck.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.idimzo.beck.entity.ContractTranslation;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContractTranslationRepository extends JpaRepository<ContractTranslation,Long> {
    List<ContractTranslation> findByTemplateId(Long id);
}