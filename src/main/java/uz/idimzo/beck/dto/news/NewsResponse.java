package uz.idimzo.beck.dto.news;

import lombok.Builder;
import lombok.Data;
import uz.idimzo.beck.dto.MultilingualTitle;
import uz.idimzo.beck.entity.MediaType;

import java.time.LocalDateTime;

@Data
@Builder
public class NewsResponse {
    private Long id;
    private MultilingualTitle title;
    private String mediaUrl;
    private MediaType mediaType;
    private String externalLink;
    private LocalDateTime publishDate;
    private LocalDateTime expiryDate;
    private boolean viewed;
    private boolean active;
}
