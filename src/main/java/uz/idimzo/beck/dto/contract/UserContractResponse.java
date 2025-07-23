package uz.idimzo.beck.dto.contract;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserContractResponse {


    private ContractTemplateRequest template;
    private String content;
    @NotBlank(message = "Language code is required")
    private String languageCode;

}