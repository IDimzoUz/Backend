package uz.idimzo.beck.dto.contract;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContractTranslationDto {
    private Long id;
    private String languageCode;
    private String content;
    private Long templateId;
    private String templateName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}