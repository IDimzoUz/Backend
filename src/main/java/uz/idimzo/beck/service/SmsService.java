package uz.idimzo.beck.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static uz.idimzo.beck.utils.RestConstants.SMS_CODE_PREFIX;

@Service
@RequiredArgsConstructor
@Slf4j
public class SmsService {

    private final RestTemplate restTemplate;
    private final RedisTemplate<String, String> redisTemplate;
    
    @Value("${sms.code.expiration}") // 5 minutes default
    private long codeExpiration;
    
    @Value("${sms.api-key}")
    private String apiKey;
    
    // Update the base URL
    @Value("${sms.base-url}")
    private String baseUrl;
    
    /**
     * Generate and send verification code to phone number
     */
    public String sendVerificationCode(String phoneNumber) {
        // Generate random 4-digit code
        String code = generateRandomCode();
        
        // Store code in Redis with expiration
        redisTemplate.opsForValue().set(
            SMS_CODE_PREFIX + phoneNumber,
            code,
            codeExpiration,
            TimeUnit.SECONDS
        );
        
        sendSms(phoneNumber, "Sizning tasdiqlash kodingiz: " + code);
        
        log.info("Verification code sent to {}: {}", phoneNumber, code);
        
        return code;
    }
    
    /**
     * Send verification code for phone number update
     */
    public String sendPhoneUpdateVerificationCode(String phoneNumber) {
        // Generate random 4-digit code
        String code = generateRandomCode();
        
        // Store code in Redis with expiration
        redisTemplate.opsForValue().set(
            SMS_CODE_PREFIX + "update:" + phoneNumber,
            code,
            codeExpiration,
            TimeUnit.SECONDS
        );
        
        sendSms(phoneNumber, "Telefon raqamni o'zgartirish uchun tasdiqlash kodi: " + code);
        
        log.info("Phone update verification code sent to {}: {}", phoneNumber, code);
        
        return code;
    }
    
    /**
     * Verify the code for a phone number
     */
    public boolean verifyCode(String phoneNumber, String code) {
        String storedCode = redisTemplate.opsForValue().get(SMS_CODE_PREFIX + phoneNumber);
        
        if (storedCode != null && storedCode.equals(code)) {
            // Delete the code after successful verification
            redisTemplate.delete(SMS_CODE_PREFIX + phoneNumber);
            return true;
        }
        
        return false;
    }
    
    /**
     * Verify the code for phone number update
     */
    public boolean verifyPhoneUpdateCode(String phoneNumber, String code) {
        String storedCode = redisTemplate.opsForValue().get(SMS_CODE_PREFIX + "update:" + phoneNumber);
        
        if (storedCode != null && storedCode.equals(code)) {
            // Delete the code after successful verification
            redisTemplate.delete(SMS_CODE_PREFIX + "update:" + phoneNumber);
            return true;
        }
        
        return false;
    }
    
    /**
     * Generate random 4-digit code
     */
    private String generateRandomCode() {
        Random random = new Random();
        int code = 1000 + random.nextInt(9000); // 4-digit code
        return String.valueOf(code);
    }
    
    /**
     * Send SMS to phone number
     */
    private void sendSms(String phoneNumber, String message) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        // Remove "+998" if present in the phone number
        String formattedPhone = phoneNumber.replace("+", "");
        
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("key", apiKey);
        requestBody.put("phone", formattedPhone);
        requestBody.put("message", message);
        
        HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);
        
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(
                    baseUrl + "/send",
                    request,
                    String.class
            );
            log.info("SMS API response: {}", response.getBody());
        } catch (Exception e) {
            log.error("Error sending SMS: {}", e.getMessage());
            throw new RuntimeException("Failed to send SMS", e);
        }
    }
}