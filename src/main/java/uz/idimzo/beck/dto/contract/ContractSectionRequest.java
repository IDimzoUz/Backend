package uz.idimzo.beck.dto.contract;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class ContractSectionRequest {
    @NotBlank
    private String name;
    
    private Integer orderIndex;
    
//    @NotEmpty
//    @Valid
    private List<ContractFieldRequest> fields;
}