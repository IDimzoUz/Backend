package uz.idimzo.beck.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePhoneRequest {
    @NotBlank(message = "Telefon raqam kiritilishi shart")
    private String phoneNumber;
    
    @NotBlank(message = "Tasdiqlash kodi kiritilishi shart")
    private String verificationCode;
}