package uz.idimzo.beck.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.idimzo.beck.dto.contract.ContractTranslationDto;
import uz.idimzo.beck.dto.contract.ContractFillRequest;
import uz.idimzo.beck.dto.contract.ContractFillResponse;
import uz.idimzo.beck.dto.contract.UserContractResponse;
import uz.idimzo.beck.service.ContractService;
import uz.idimzo.beck.utils.RestConstants;

import java.util.List;

@RestController
@RequestMapping(RestConstants.TM_BASE_URL + "/user/contracts")
@RequiredArgsConstructor
public class UserContractController {

    private final ContractService contractService;

    /**
     * Create a new contract based on template and language
     */
    @PostMapping("/{templateId}/{languageCode}")
    public ResponseEntity<ContractFillResponse> fillContract(
            @PathVariable Long templateId,
            @PathVariable String languageCode,
            @Valid @RequestBody ContractFillRequest request) {
        ContractFillResponse response = contractService.fillContract(templateId, languageCode, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Get all contracts created by the current user
     */
    @GetMapping
    public ResponseEntity<List<ContractFillResponse>> getUserContracts() {
        return ResponseEntity.ok(contractService.getUserContracts());
    }

    /**
     * Get a specific contract by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ContractFillResponse> getContractById(@PathVariable Long id) {
        return ResponseEntity.ok(contractService.getContractById(id));
    }
}