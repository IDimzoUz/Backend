package uz.idimzo.beck.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VerifyRequest {
    
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+998[0-9]{9}$", message = "Phone number should be in format +998XXXXXXXXX")
    private String phoneNumber;
    
    @NotBlank(message = "Verification code is required")
    private String code;
}