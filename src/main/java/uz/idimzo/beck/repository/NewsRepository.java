package uz.idimzo.beck.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.idimzo.beck.entity.News;

import java.time.LocalDateTime;
import java.util.List;

public interface NewsRepository extends JpaRepository<News, Long> {
    @Query("SELECT n FROM News n WHERE n.active = true AND n.publishDate <= :now AND n.expiryDate > :now")
    List<News> findActiveNews(LocalDateTime now);
    
    @Query("SELECT n FROM News n WHERE n.publishDate BETWEEN :start AND :end")
    List<News> findUpcomingNews(LocalDateTime start, LocalDateTime end);
}