package uz.idimzo.beck.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.idimzo.beck.dto.category.CategoryDto;
import uz.idimzo.beck.dto.category.CreateCategoryRequest;
import uz.idimzo.beck.entity.Category;
import uz.idimzo.beck.repository.CategoryRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<CategoryDto> getAllCategories() {
        List<Category> topLevelCategories = categoryRepository.findAllTopLevelCategories();
        return topLevelCategories.stream()
                .map(this::convertToDtoWithChildren)
                .collect(Collectors.toList());
    }

    @Transactional
    public CategoryDto createCategory(CreateCategoryRequest request) {
        // Check if category with same name exists at same level
        if (categoryRepository.existsByNameUzAndNameUzCyrlAndNameKaaAndNameRuAndNameEnAndParentId(request.getNameUz(), request.getNameUzCyrl(),
                request.getNameKaa(), request.getNameRu(), request.getNameEn(), request.getParentId())) {
            throw new IllegalArgumentException("Category with this name already exists at this level");
        }

        Category category = Category.builder()
                .nameUz(request.getNameUz())
                .nameUzCyrl(request.getNameUzCyrl())
                .nameKaa(request.getNameKaa())
                .nameRu(request.getNameRu())
                .nameEn(request.getNameEn())
                .parent(request.getParentId() != null ?
                        categoryRepository.findById(request.getParentId())
                                .orElseThrow(() -> new IllegalArgumentException("Parent category not found")) :
                        null)
                .build();

        Category savedCategory = categoryRepository.save(category);
        return convertToDto(savedCategory);
    }

    @Transactional
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    @Transactional
    public CategoryDto updateCategory(Long id, CreateCategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        // Check if new name conflicts with existing categories at same level
        if (!category.getNameUz().equals(request.getNameUz()) &&
                categoryRepository.existsByNameUzAndNameUzCyrlAndNameKaaAndNameRuAndNameEnAndParentId(request.getNameUz(), request.getNameUzCyrl(),
                        request.getNameKaa(), request.getNameRu(), request.getNameEn(), request.getParentId())) {
            throw new IllegalArgumentException("Category with this name already exists at this level");
        }

        if (request.getNameUz() != null && !request.getNameUz().equals(category.getNameUz())) {
            category.setNameUz(request.getNameUz());
        }

        if (request.getNameUzCyrl() != null && !request.getNameUzCyrl().equals(category.getNameUzCyrl())) {
            category.setNameUzCyrl(request.getNameUzCyrl());
        }

        if (request.getNameKaa() != null && !request.getNameKaa().equals(category.getNameKaa())) {
            category.setNameKaa(request.getNameKaa());
        }

        if (request.getNameRu() != null && !request.getNameRu().equals(category.getNameRu())) {
            category.setNameRu(request.getNameRu());
        }

        if (request.getNameEn() != null && !request.getNameEn().equals(category.getNameEn())) {
            category.setNameEn(request.getNameEn());
        }

        if (request.getParentId() != null && !request.getParentId().equals(category.getParent() != null ? category.getParent().getId() : null)) {
            Category newParent = categoryRepository.findById(request.getParentId())
                    .orElseThrow(() -> new IllegalArgumentException("Parent category not found"));
            category.setParent(newParent);
        }

        Category updatedCategory = categoryRepository.save(category);
        return convertToDto(updatedCategory);
    }
    /* add tree
    @Transactional
    public CategoryDto createCategoryTree(CategoryTreeRequest request) {
        Category rootCategory = createCategoryWithChildren(request.getName(), request.getChildren(), null);
        return convertToDtoWithChildren(rootCategory);
    }

    private Category createCategoryWithChildren(String name, Map<String, Object> children, Category parent) {
        Category category = Category.builder()
                .name(name)
                .parent(parent)
                .build();

        // Avval saqlaymiz
        category = categoryRepository.save(category);

        // Agar parent bo'lsa bog'laymiz
        if (parent != null) {
            parent.addChild(category);
            categoryRepository.save(parent);  // Parentni yangilash
        }

        if (children != null) {
            processChildren(children, category);
        }

        return category;
    }
    private void processChildren(Map<String, Object> children, Category parent) {
        for (Map.Entry<String, Object> entry : children.entrySet()) {
            if (entry.getValue() instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> childrenMap = (Map<String, Object>) entry.getValue();
                createCategoryWithChildren(entry.getKey(), childrenMap, parent);
            } else if (entry.getValue() instanceof List) {
                processListChildren(entry.getKey(), (List<?>) entry.getValue(), parent);
            } else if (entry.getValue() instanceof String) {
                createCategoryWithChildren((String) entry.getValue(), null, parent);
            }
        }
    }
    private void processListChildren(String parentName, List<?> childrenList, Category grandParent) {
        Category parentCategory = createCategoryWithChildren(parentName, null, grandParent);

        for (Object child : childrenList) {
            if (child instanceof String) {
                createCategoryWithChildren((String) child, null, parentCategory);
            }
        }
}*/

    private CategoryDto convertToDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .nameUz(category.getNameUz())
                .nameUzCyrl(category.getNameUzCyrl())
                .nameKaa(category.getNameKaa())
                .nameRu(category.getNameRu())
                .nameEn(category.getNameEn())
                .parentId(category.getParent() != null ? category.getParent().getId() : null)
                .build();
    }

    private CategoryDto convertToDtoWithChildren(Category category) {
        CategoryDto dto = new CategoryDto();
        dto.setId(category.getId());
        dto.setNameUz(category.getNameUz());
        dto.setNameUzCyrl(category.getNameUzCyrl());
        dto.setNameKaa(category.getNameKaa());
        dto.setNameRu(category.getNameRu());
        dto.setNameEn(category.getNameEn());

        // LAZY loading ishlashi uchun @Transactional kerak
        if (category.getChildren() != null && !category.getChildren().isEmpty()) {
            dto.setChildren(category.getChildren().stream()
                    .map(this::convertToDtoWithChildren)
                    .collect(Collectors.toList()));
        }

        return dto;
    }
}