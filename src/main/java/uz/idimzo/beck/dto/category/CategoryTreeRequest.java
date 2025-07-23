package uz.idimzo.beck.dto.category;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
public class CategoryTreeRequest {
    private String name;
    private Map<String, Object> children;
}

//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//public class MultilingualName {
//    @NotBlank(message = "O'zbekcha nomi kiritilishi shart")
//    private String uz;
//    private String uzCyrl;
//    private String kaa;
//    private String ru;
//    private String en;
//}