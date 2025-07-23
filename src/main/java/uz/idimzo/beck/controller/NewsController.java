package uz.idimzo.beck.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.idimzo.beck.dto.news.NewsCreateRequest;
import uz.idimzo.beck.dto.news.NewsResponse;
import uz.idimzo.beck.entity.User;
import uz.idimzo.beck.service.NewsService;
import uz.idimzo.beck.service.FileStorageService;

import java.util.List;

@RestController
@RequestMapping("/api/news")
@RequiredArgsConstructor
public class NewsController {
    private final NewsService newsService;
    private final FileStorageService fileStorageService;

    @PostMapping("/upload")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        String fileName = fileStorageService.storeFile(file);
        return ResponseEntity.ok(fileName);
    }
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<NewsResponse> createNews(@RequestBody NewsCreateRequest request) {
        return ResponseEntity.ok(newsService.createNews(request));
    }
    
    @GetMapping
    public ResponseEntity<List<NewsResponse>> getActiveNews() {
        return ResponseEntity.ok(newsService.getActiveNews());
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<NewsResponse>> getAllNews() {
        return ResponseEntity.ok(newsService.getAllNews());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<NewsResponse> getNewsById(@PathVariable Long id) {
        return ResponseEntity.ok(newsService.getNewsById(id));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<NewsResponse> updateNews(
            @PathVariable Long id,
            @RequestBody NewsCreateRequest request) {
        return ResponseEntity.ok(newsService.updateNews(id, request));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteNews(@PathVariable Long id) {
        newsService.deleteNews(id);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/{id}/view")
    public ResponseEntity<Void> markAsViewed(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {
        newsService.markAsViewed(id, user);
        return ResponseEntity.ok().build();
    }
}