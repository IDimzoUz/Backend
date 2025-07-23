package uz.idimzo.beck.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "contract_fields")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContractField {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "field_id", nullable = false, unique = true)
    private String fieldId;
    
    @Column(name = "name", nullable = false)
    private String name;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "field_type", nullable = false)
    private FieldType fieldType;
    
    @Column(name = "required")
    private boolean required;
    
    @Column(name = "min_value")
    private Integer minValue;
    
    @Column(name = "max_value")
    private Integer maxValue;
    
    @Column(name = "min_length")
    private Integer minLength;
    
    @Column(name = "max_length")
    private Integer maxLength;
    
    @Column(name = "pattern")
    private String pattern;
    
    @ElementCollection
    @CollectionTable(name = "field_options", joinColumns = @JoinColumn(name = "field_id"))
    @Column(name = "option_value")
    private List<String> options = new ArrayList<>();
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id")
    private ContractSection section;

    public void validateValue(String value) {
        switch (fieldType) {
            case INTEGER:
                try {
                    double numValue = Double.parseDouble(value);
                    if (minValue != null && numValue < minValue) {
                        throw new IllegalArgumentException(name + ": qiymat " + minValue + " dan kichik bo'lmasligi kerak");
                    }
                    if (maxValue != null && numValue > maxValue) {
                        throw new IllegalArgumentException(name + ": qiymat " + maxValue + " dan katta bo'lmasligi kerak");
                    }
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException(name + ": son kiritilishi kerak");
                }
                break;

            case TEXT:
                if (minLength != null && value.length() < minLength) {
                    throw new IllegalArgumentException(name + ": matn uzunligi " + minLength + " belgidan kam bo'lmasligi kerak");
                }
                if (maxLength != null && value.length() > maxLength) {
                    throw new IllegalArgumentException(name + ": matn uzunligi " + maxLength + " belgidan ko'p bo'lmasligi kerak");
                }
                if (pattern != null && !value.matches(pattern)) {
                    throw new IllegalArgumentException(name + ": noto'g'ri format");
                }
                break;

            case DATE:
                try {
                    LocalDate.parse(value);
                } catch (DateTimeParseException e) {
                    throw new IllegalArgumentException(name + ": sana formati noto'g'ri (YYYY-MM-DD)");
                }
                break;

            case DROPDOWN:
                if (options != null && !options.contains(value)) {
                    throw new IllegalArgumentException(name + ": tanlov variantlari orasidan tanlash kerak");
                }
                break;
        }
    }
    
    private Integer orderIndex;
    
    @Column(name = "placeholder")
    private String placeholder;

    @Column(name = "help_text")
    private String helpText;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    public void generateFieldId() {
        if (fieldId == null) {
            fieldId = UUID.randomUUID().toString().replace("-", "").substring(0, 9);
        }
    }

    @PrePersist
    protected void onCreate() {
        generateFieldId();
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    public String getPlaceholderTag() {
        return String.format("{#%s}", fieldId);
    }
}