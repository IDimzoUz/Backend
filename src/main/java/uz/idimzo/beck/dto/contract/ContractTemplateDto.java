package uz.idimzo.beck.dto.contract;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContractTemplateDto {
    private Long id;
    private String name;
    private List<String> availableLanguages;
}