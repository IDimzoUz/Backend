package uz.idimzo.beck.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import uz.idimzo.beck.dto.news.NewsNotification;
import uz.idimzo.beck.entity.News;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebSocketService {
    
    private final SimpMessagingTemplate messagingTemplate;
    
    public void sendNewsNotification(News news) {
        NewsNotification notification = new NewsNotification(
            news.getId(),
            "Yangi e'lon qo'shildi!",
            news.getTitleUz(),
            news.getTitleRu(),
            news.getTitleEn()
        );

        try {
            messagingTemplate.convertAndSend("/topic/news", notification);
        } catch (Exception e) {
            log.error("❌ Notification yuborishda xatolik:", e);
        }
    }
    
    public void sendNewsExpirationNotification(News news) {
        NewsNotification notification = new NewsNotification(
            news.getId(),
            "E'lon muddati tugadi",
            news.getTitleUz(),
            news.getTitleRu(),
            news.getTitleEn()
        );
        
        messagingTemplate.convertAndSend("/topic/news/expired", notification);
    }
}