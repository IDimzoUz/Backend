package uz.idimzo.beck.dto.category;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateCategoryRequest {
    @NotBlank(message = "Category nameUz is required")
    private String nameUz;
    @NotBlank(message = "Category nameUzCyrl is required")
    private String nameUzCyrl;
    @NotBlank(message = "Category nameKaa is required")
    private String nameKaa;
    @NotBlank(message = "Category nameRu is required")
    private String nameRu;
    @NotBlank(message = "Category nameEn is required")
    private String nameEn;
    private Long parentId;
}