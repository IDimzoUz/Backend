package uz.idimzo.beck.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MultilingualTitle {
    private String uz;
    private String uzCyrl;
    private String kaa;
    private String ru;
    private String en;
}
