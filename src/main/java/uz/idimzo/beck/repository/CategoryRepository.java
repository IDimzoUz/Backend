package uz.idimzo.beck.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.idimzo.beck.entity.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    
    @Query("SELECT c FROM Category c WHERE c.parent IS NULL")
    List<Category> findAllTopLevelCategories();
    @EntityGraph(attributePaths = {"children"})
    Optional<Category> findWithChildrenById(Long id);
    @Query("SELECT c FROM Category c LEFT JOIN FETCH c.children WHERE c.id = :id")
    Optional<Category> findByIdWithChildren(@Param("id") Long id);
    
    List<Category> findByParentId(Long parentId);
    
    boolean existsByNameUzAndParentId(String name, Long parentId);
    boolean existsByNameUzAndNameUzCyrlAndNameKaaAndNameRuAndNameEnAndParentId(String nameUz,String nameUzCyrl,String nameKaa,String nameRu,String nameEn, Long parentId);
}