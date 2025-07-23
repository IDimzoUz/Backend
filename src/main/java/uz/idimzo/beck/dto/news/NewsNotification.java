package uz.idimzo.beck.dto.news;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewsNotification {
    private Long newsId;
    private String message;
    private String titleUz;
    private String titleRu;
    private String titleEn;
}