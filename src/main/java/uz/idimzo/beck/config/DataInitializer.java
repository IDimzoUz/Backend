package uz.idimzo.beck.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import uz.idimzo.beck.entity.Role;
import uz.idimzo.beck.entity.User;
import uz.idimzo.beck.repository.UserRepository;

import java.time.LocalDateTime;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initData() {
        return args -> {
            // Only seed if the database is empty
            if (userRepository.count() == 0) {
                log.info("Starting database initialization...");
                
                // Create ADMIN user
                User admin = User.builder()
                        .firstName("Admin")
                        .lastName("User")
                        .email("admin@example.com")
                        .password(passwordEncoder.encode("admin123"))
                        .phoneNumber("+998901234567")
                        .role(Role.ADMIN)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .enabled(true)
                        .accountNonExpired(true)
                        .accountNonLocked(true)
                        .credentialsNonExpired(true)
                        .build();
                userRepository.save(admin);
                log.info("Admin user created");
                
                // Create LEGAL_ENTITY user
                User legalEntity = User.builder()
                        .firstName("Legal")
                        .lastName("Entity")
                        .email("legal@example.com")
                        .password(passwordEncoder.encode("legal123"))
                        .phoneNumber("+998902345678")
                        .role(Role.LEGAL_ENTITY)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .enabled(true)
                        .accountNonExpired(true)
                        .accountNonLocked(true)
                        .credentialsNonExpired(true)
                        .build();
                userRepository.save(legalEntity);
                log.info("Legal entity user created");
                
                // Create INDIVIDUAL user
                User individual = User.builder()
                        .firstName("Individual")
                        .lastName("User")
                        .email("individual@example.com")
                        .password(passwordEncoder.encode("individual123"))
                        .phoneNumber("+998903456789")
                        .role(Role.INDIVIDUAL)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .enabled(true)
                        .accountNonExpired(true)
                        .accountNonLocked(true)
                        .credentialsNonExpired(true)
                        .build();
                userRepository.save(individual);
                log.info("Individual user created");
                
                log.info("Database initialization completed!");
            } else {
                log.info("Database already contains data, skipping initialization");
            }
        };
    }
}