package uz.idimzo.beck.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.idimzo.beck.dto.user.UpdatePhoneRequest;
import uz.idimzo.beck.dto.user.UpdateProfileRequest;
import uz.idimzo.beck.dto.user.UserResponse;
import uz.idimzo.beck.entity.User;
import uz.idimzo.beck.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final SmsService smsService;

    /**
     * Get current authenticated user
     */
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String phoneNumber = authentication.getName();
        return userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new IllegalArgumentException("Foydalanuvchi topilmadi"));
    }

    /**
     * Get current user profile
     */
    public UserResponse getCurrentUserProfile() {
        User user = getCurrentUser();
        return mapToUserResponse(user);
    }

    /**
     * Update user profile
     */
    @Transactional
    public UserResponse updateProfile(UpdateProfileRequest request) {
        User user = getCurrentUser();
        
        if (request.getFirstName() != null) {
            user.setFirstName(request.getFirstName());
        }
        
        if (request.getLastName() != null) {
            user.setLastName(request.getLastName());
        }
        
        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }
        
        if (request.getProfilePhotoUrl() != null) {
            user.setProfilePhotoUrl(request.getProfilePhotoUrl());
        }
        
        userRepository.save(user);
        
        return mapToUserResponse(user);
    }

    /**
     * Update phone number with verification
     */
    @Transactional
    public UserResponse updatePhoneNumber(UpdatePhoneRequest request) {
        User user = getCurrentUser();
        
        // Verify the code using the phone update verification method
        boolean isCodeValid = smsService.verifyPhoneUpdateCode(request.getPhoneNumber(), request.getVerificationCode());
        
        if (!isCodeValid) {
            throw new IllegalArgumentException("Noto'g'ri tasdiqlash kodi");
        }
        
        // Check if phone number is already used
        if (userRepository.existsByPhoneNumber(request.getPhoneNumber()) && 
            !request.getPhoneNumber().equals(user.getPhoneNumber())) {
            throw new IllegalArgumentException("Bu telefon raqam boshqa foydalanuvchi tomonidan ishlatilmoqda");
        }
        
        // Update phone number
        user.setPhoneNumber(request.getPhoneNumber());
        userRepository.save(user);
        
        return mapToUserResponse(user);
    }
    
    /**
     * Map User entity to UserResponse DTO
     */
    private UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .profilePhotoUrl(user.getProfilePhotoUrl())
                .role(user.getRole())
                .build();
    }
}