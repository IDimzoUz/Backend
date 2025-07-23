package uz.idimzo.beck.dto.contract;

import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContractTranslationResponse {
    private Long id;
    private String languageCode;
    private String content;
    private List<ContractSectionResponse> sections = new ArrayList<>();
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}