package uz.idimzo.beck.dto.contract;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContractSectionResponse {
    private Long id;
    private String name;
    private Integer orderIndex;
    private List<ContractFieldResponse> fields = new ArrayList<>();
}