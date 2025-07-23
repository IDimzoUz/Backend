package uz.idimzo.beck.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.idimzo.beck.entity.News;
import uz.idimzo.beck.entity.NewsView;
import uz.idimzo.beck.entity.User;

public interface NewsViewRepository extends JpaRepository<NewsView, Long> {
    boolean existsByNewsAndUser(News news, User user);
}
