package uz.idimzo.beck.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class AdminLoginRequest {
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+998[0-9]{9}$", message = "Phone number should be in format +998XXXXXXXXX")
    private String phoneNumber;

    @NotBlank(message = "Password is required")
    private String password;
}