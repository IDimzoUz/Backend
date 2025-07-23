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
public class ContractTemplateResponse {
    private Long id;
    private String name;
//    private Long categoryId;
//    private String categoryName;
    private List<ContractTranslationResponse> translations = new ArrayList<>();
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}