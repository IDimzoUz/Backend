package uz.idimzo.beck.dto.contract;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.Map;

@Data
public class ContractFillRequest {
    @NotEmpty
    private Map<String, String> fieldValues; // key: fieldId, value: kiritilgan qiymat
}