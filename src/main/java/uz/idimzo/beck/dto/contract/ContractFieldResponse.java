package uz.idimzo.beck.dto.contract;

import lombok.*;
import uz.idimzo.beck.entity.FieldType;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContractFieldResponse {
    private Long id;
    private String name;
    private String fieldId;
    private FieldType fieldType;
    private boolean required;
    private Double minValue;
    private Double maxValue;
    private Integer minLength;
    private Integer maxLength;
    private String pattern;
    private List<String> options = new ArrayList<>();
    private Integer orderIndex;
    private String placeholder;
    private String helpText;
}