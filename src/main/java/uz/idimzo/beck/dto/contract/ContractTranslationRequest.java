package uz.idimzo.beck.dto.contract;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class ContractTranslationRequest {
    @NotBlank
    private String languageCode; // uz, uz_cyrl, kaa, ru, en
    
    @NotBlank
    private String content;
    
    @NotEmpty
    @Valid
    private List<ContractSectionRequest> sections;
}