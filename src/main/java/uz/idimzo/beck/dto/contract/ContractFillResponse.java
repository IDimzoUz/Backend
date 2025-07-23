package uz.idimzo.beck.dto.contract;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContractFillResponse {
    private Long templateId;
    private String languageCode;
    private String filledContent;
    private String templateName;
}