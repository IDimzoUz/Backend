package uz.idimzo.beck.dto.news;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import uz.idimzo.beck.entity.MediaType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class NewsCreateRequest {
    @NotBlank
    private String titleUz;
    private String titleUzCyrl;
    private String titleKaa;
    private String titleRu;
    private String titleEn;
    
    @NotBlank
    private String mediaUrl;
    
    private String externalLink;
    
    @NotNull
    private LocalDateTime publishDate;
    
    @NotNull
    private LocalDateTime expiryDate;
}

