package uz.idimzo.beck.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.idimzo.beck.dto.auth.AdminLoginRequest;
import uz.idimzo.beck.dto.auth.AuthResponse;
import uz.idimzo.beck.dto.auth.SmsRequest;
import uz.idimzo.beck.dto.auth.VerifyRequest;
import uz.idimzo.beck.entity.Role;
import uz.idimzo.beck.entity.User;
import uz.idimzo.beck.repository.UserRepository;
import uz.idimzo.beck.security.JwtService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final SmsService smsService;

    /**
     * Send SMS verification code for login or registration
     */
    public String sendVerificationCode(SmsRequest request) {
        return smsService.sendVerificationCode(request.getPhoneNumber());
    }

    /**
     * Verify SMS code and register or login user
     */
    public AuthResponse verifyAndAuthenticate(VerifyRequest request) {
        // Verify the code
        boolean isCodeValid = smsService.verifyCode(request.getPhoneNumber(), request.getCode());
        
        if (!isCodeValid) {
            throw new IllegalArgumentException("Invalid verification code");
        }
        
        // Check if user exists
        Optional<User> existingUser = userRepository.findByPhoneNumber(request.getPhoneNumber());
        
        if (existingUser.isEmpty()) {
            throw new IllegalArgumentException("User not found. Please register first.");
        }
        
        User user = existingUser.get();
        
        // Generate tokens
        var accessToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
    
    /**
     * Generate a random password for users authenticated via SMS
     */
    private String generateRandomPassword() {
        return java.util.UUID.randomUUID().toString().substring(0,4);
    }

    public AuthResponse adminLogin(AdminLoginRequest request) {
        try {
            // Find user by phone number
            User user = userRepository.findByPhoneNumber(request.getPhoneNumber())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));
    
            // Verify that user is an admin
            if (!Role.ADMIN.equals(user.getRole())) {
                throw new IllegalArgumentException("Invalid credentials");
            }
    
            // Verify password
            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                throw new IllegalArgumentException("Invalid credentials");
            }
    
            // Generate tokens
            var accessToken = jwtService.generateToken(user);
            var refreshToken = jwtService.generateRefreshToken(user);
    
            return AuthResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid credentials");
        }
    }
    
    public AuthResponse refreshToken(String refreshToken) {
        if (refreshToken == null) {
            throw new IllegalArgumentException("Refresh token is required");
        }

        // Extract phone number from refresh token
        String phoneNumber = jwtService.extractPhoneNumber(refreshToken);
        
        // Validate refresh token and get user
        User user = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        if (!jwtService.isTokenValid(refreshToken, user)) {
            throw new IllegalArgumentException("Invalid refresh token");
        }
        
        // Generate new tokens
        String newAccessToken = jwtService.generateToken(user);
        String newRefreshToken = jwtService.generateRefreshToken(user);
        
        return AuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }
    public void createUser(SmsRequest smsRequest){
        if (userRepository.existsByPhoneNumber(smsRequest.getPhoneNumber())) {
            return;
        }else {
            User user = new User();
            user.setRole(Role.INDIVIDUAL);
            user.setPhoneNumber(smsRequest.getPhoneNumber());
            userRepository.save(user);
        }
    }
}