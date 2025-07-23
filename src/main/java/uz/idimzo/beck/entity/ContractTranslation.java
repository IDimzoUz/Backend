package uz.idimzo.beck.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "contract_translations")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContractTranslation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "language_code", nullable = false)
    private String languageCode; // uz, uz_cyrl, kaa, ru, en
    
    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;
    
    @OneToMany(mappedBy = "translation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ContractSection> sections = new ArrayList<>();
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id")
    private ContractTemplate template;
    
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