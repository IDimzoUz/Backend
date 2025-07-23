package uz.idimzo.beck.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.idimzo.beck.dto.*;
import uz.idimzo.beck.dto.auth.AdminLoginRequest;
import uz.idimzo.beck.dto.auth.AuthResponse;
import uz.idimzo.beck.dto.auth.SmsRequest;
import uz.idimzo.beck.dto.auth.VerifyRequest;
import uz.idimzo.beck.service.AuthService;
import uz.idimzo.beck.utils.RestConstants;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(RestConstants.TM_BASE_URL + "/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/send-code")
    public ResponseEntity<Map<String, String>> sendVerificationCode(
            @Valid @RequestBody SmsRequest request
    ) {
        authService.createUser(request);
        String code = authService.sendVerificationCode(request);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Verification code sent successfully");
        // In production, don't return the code in the response
        // This is just for testing purposes
        response.put("code", code);
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify")
    public ResponseEntity<AuthResponse> verifyAndAuthenticate(
            @Valid @RequestBody VerifyRequest request
    ) {
        return ResponseEntity.ok(authService.verifyAndAuthenticate(request));
    }

    @PostMapping("/admin/login")
    public ResponseEntity<AuthResponse> adminLogin(@Valid @RequestBody AdminLoginRequest request) {
        return ResponseEntity.ok(authService.adminLogin(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(authService.refreshToken(request.getRefreshToken()));
    }
}