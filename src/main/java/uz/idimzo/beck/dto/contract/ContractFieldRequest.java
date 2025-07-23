package uz.idimzo.beck.dto.contract;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import uz.idimzo.beck.entity.FieldType;

import java.util.List;

@Data
public class ContractFieldRequest {
    @NotBlank
    private String name;
    
    private String fieldId;
    
    @NotNull
    private FieldType fieldType;
    
    private boolean required;
    
    private Integer minValue;
    private Integer maxValue;
    private Integer minLength;
    private Integer maxLength;
    private String pattern;
    private List<String> options;
    private Integer orderIndex;
    private String placeholder;
    private String helpText;
    
    public void validateFieldType() {
        switch (fieldType) {
            case MONEY, INTEGER -> {
                if (minValue != null && maxValue != null && minValue > maxValue) {
                    throw new IllegalArgumentException("Minimal qiymat maksimal qiymatdan katta bo'lishi mumkin emas");
                }
            }
            case DROPDOWN -> {
                if (options == null || options.isEmpty()) {
                    throw new IllegalArgumentException("Dropdown maydoni uchun tanlov variantlari kiritilishi shart");
                }
            }
            case PHONE -> {
                if (pattern == null) {
                    pattern = "^\\+998[0-9]{9}$";
                }
            }
            case DOCUMENT_ID -> {
                if (pattern == null) {
                    // Pasport seriya va JSHSHIR uchun pattern
                    pattern = "^([A-Z]{2}[0-9]{7}|[0-9]{14})$";
                }
            }
            case STRING -> {
                if (minLength != null && maxLength != null && minLength > maxLength) {
                    throw new IllegalArgumentException("Minimal uzunlik maksimal uzunlikdan katta bo'lishi mumkin emas");
                }
            }
        }
    }
}