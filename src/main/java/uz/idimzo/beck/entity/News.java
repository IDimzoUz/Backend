package uz.idimzo.beck.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "news")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class News {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "title_uz", nullable = false)
    private String titleUz;
    
    @Column(name = "title_uz_cyrl")
    private String titleUzCyrl;
    
    @Column(name = "title_kaa")
    private String titleKaa;
    
    @Column(name = "title_ru")
    private String titleRu;
    
    @Column(name = "title_en")
    private String titleEn;
    
    @Column(name = "media_url", nullable = false)
    private String mediaUrl;
    
    @Column(name = "media_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private MediaType mediaType;
    
    @Column(name = "external_link")
    private String externalLink;
    
    @Column(name = "publish_date", nullable = false)
    private LocalDateTime publishDate;
    
    @Column(name = "expiry_date", nullable = false)
    private LocalDateTime expiryDate;
    
    private boolean active;
    
    @OneToMany(mappedBy = "news", cascade = CascadeType.ALL)
    private Set<NewsView> views = new HashSet<>();
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

