package uz.idimzo.beck.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.idimzo.beck.dto.auth.SmsRequest;
import uz.idimzo.beck.dto.user.UpdatePhoneRequest;
import uz.idimzo.beck.dto.user.UpdateProfileRequest;
import uz.idimzo.beck.dto.user.UserResponse;
import uz.idimzo.beck.service.SmsService;
import uz.idimzo.beck.service.UserService;
import uz.idimzo.beck.utils.RestConstants;

@RestController
@RequestMapping(RestConstants.TM_BASE_URL + "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final SmsService smsService;

    /**
     * Get current user profile
     */
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser() {
        UserResponse userResponse = userService.getCurrentUserProfile();
        return ResponseEntity.ok(userResponse);
    }

    /**
     * Update user profile
     */
    @PutMapping("/profile")
    public ResponseEntity<UserResponse> updateProfile(@Valid @RequestBody UpdateProfileRequest request) {
        UserResponse userResponse = userService.updateProfile(request);
        return ResponseEntity.ok(userResponse);
    }

    /**
     * Send verification code for phone number update
     */
    @PostMapping("/phone/send-code")
    public ResponseEntity<String> sendVerificationCodeForPhoneUpdate(@Valid @RequestBody SmsRequest request) {
        String code = smsService.sendPhoneUpdateVerificationCode(request.getPhoneNumber());
        return ResponseEntity.ok("Tasdiqlash kodi yuborildi");
    }

    /**
     * Update phone number with verification
     */
    @PutMapping("/phone")
    public ResponseEntity<UserResponse> updatePhoneNumber(@Valid @RequestBody UpdatePhoneRequest request) {
        UserResponse userResponse = userService.updatePhoneNumber(request);
        return ResponseEntity.ok(userResponse);
    }
}