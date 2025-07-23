package uz.idimzo.beck.dto.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {
    private Long id;
    private String nameUz;
    private String nameUzCyrl;
    private String nameKaa;
    private String nameRu;
    private String nameEn;
    private Long parentId;
    private List<CategoryDto> children;
}