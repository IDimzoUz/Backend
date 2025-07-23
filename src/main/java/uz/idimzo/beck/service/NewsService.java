package uz.idimzo.beck.service;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import uz.idimzo.beck.dto.MultilingualTitle;
import uz.idimzo.beck.dto.news.NewsCreateRequest;
import uz.idimzo.beck.dto.news.NewsResponse;
import uz.idimzo.beck.entity.MediaType;
import uz.idimzo.beck.entity.News;
import uz.idimzo.beck.entity.NewsView;
import uz.idimzo.beck.entity.User;
import uz.idimzo.beck.repository.NewsRepository;
import uz.idimzo.beck.repository.NewsViewRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NewsService {
    private final NewsRepository newsRepository;
    private final WebSocketService webSocketService;
    
    @Scheduled(fixedDelay = 60000) // Har minutda tekshirish
    public void checkNewsStatus() {
        LocalDateTime now = LocalDateTime.now();
        
        // Yangi chop etilgan yangiliklar uchun notification
        List<News> newlyPublished = newsRepository.findUpcomingNews(
            now.minusMinutes(1), 
            now
        );
        
        for (News news : newlyPublished) {
            webSocketService.sendNewsNotification(news);
        }
        
        // Muddati tugagan yangiliklar uchun notification
//        List<News> expired = newsRepository.findExpiredNews(
//            now.minusMinutes(1),
//            now
//        );
//
//        for (News news : expired) {
//            webSocketService.sendNewsExpirationNotification(news);
//            news.setActive(false);
//            newsRepository.save(news);
//        }
    }
    private final NewsViewRepository newsViewRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public NewsResponse createNews(NewsCreateRequest request) {
        News news = News.builder()
                .titleUz(request.getTitleUz())
                .titleUzCyrl(request.getTitleUzCyrl())
                .titleKaa(request.getTitleKaa())
                .titleRu(request.getTitleRu())
                .titleEn(request.getTitleEn())
                .mediaUrl(request.getMediaUrl())
                .mediaType(determineMediaType(request.getMediaUrl()))
                .externalLink(request.getExternalLink())
                .publishDate(request.getPublishDate())
                .expiryDate(request.getExpiryDate())
                .active(true)
                .build();
        
        return convertToDto(newsRepository.save(news));
    }

    public List<NewsResponse> getActiveNews() {
        return newsRepository.findActiveNews(LocalDateTime.now())
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<NewsResponse> getAllNews() {
        return newsRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public NewsResponse getNewsById(Long id) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("News not found"));
        return convertToDto(news);
    }

    public NewsResponse updateNews(Long id, NewsCreateRequest request) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("News not found"));

        news.setTitleUz(request.getTitleUz());
        news.setTitleUzCyrl(request.getTitleUzCyrl());
        news.setTitleKaa(request.getTitleKaa());
        news.setTitleRu(request.getTitleRu());
        news.setTitleEn(request.getTitleEn());
        news.setMediaUrl(request.getMediaUrl());
        news.setMediaType(determineMediaType(request.getMediaUrl()));
        news.setExternalLink(request.getExternalLink());
        news.setPublishDate(request.getPublishDate());
        news.setExpiryDate(request.getExpiryDate());

        return convertToDto(newsRepository.save(news));
    }

    public void deleteNews(Long id) {
        newsRepository.deleteById(id);
    }

    public void markAsViewed(Long newsId, User user) {
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new IllegalArgumentException("News not found"));
        
        if (!newsViewRepository.existsByNewsAndUser(news, user)) {
            NewsView view = new NewsView();
            view.setNews(news);
            view.setUser(user);
            view.setViewedAt(LocalDateTime.now());
            newsViewRepository.save(view);
        }
    }

    private MediaType determineMediaType(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        return switch (extension) {
            case "webp" -> MediaType.WEBP;
            case "png" -> MediaType.PNG;
            case "jpg", "jpeg" -> MediaType.JPG;
            case "mp4" -> MediaType.MP4;
            case "webm" -> MediaType.WEBM;
            case "mov" -> MediaType.MOV;
            default -> throw new IllegalArgumentException("Unsupported media type");
        };
    }

    private NewsResponse convertToDto(News news) {
        return NewsResponse.builder()
                .id(news.getId())
                .title(new MultilingualTitle(
                        news.getTitleUz(),
                        news.getTitleUzCyrl(),
                        news.getTitleKaa(),
                        news.getTitleRu(),
                        news.getTitleEn()
                ))
                .mediaUrl(news.getMediaUrl())
                .mediaType(news.getMediaType())
                .externalLink(news.getExternalLink())
                .publishDate(news.getPublishDate())
                .expiryDate(news.getExpiryDate())
                .active(news.isActive())
                .build();
    }
}