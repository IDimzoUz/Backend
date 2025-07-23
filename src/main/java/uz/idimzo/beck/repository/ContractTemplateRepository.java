package uz.idimzo.beck.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.idimzo.beck.entity.ContractTemplate;

import java.util.List;

@Repository
public interface ContractTemplateRepository extends JpaRepository<ContractTemplate, Long> {
    
    List<ContractTemplate> findByCategoryId(Long categoryId);
    
    @Query("SELECT DISTINCT ct.languageCode FROM ContractTranslation ct WHERE ct.template.id = :templateId")
    List<String> findAvailableLanguagesByTemplateId(@Param("templateId") Long templateId);
}