package uz.idimzo.beck.dto.contract;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class ContractTemplateRequest {
    @NotBlank
    private String name;
    
    @NotNull
    private Long categoryId;
    
    @NotEmpty
    @Valid
    private List<ContractTranslationRequest> translations;
}