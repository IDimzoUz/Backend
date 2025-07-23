package uz.idimzo.beck.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.idimzo.beck.dto.contract.ContractTemplateRequest;
import uz.idimzo.beck.dto.contract.ContractTemplateResponse;
import uz.idimzo.beck.service.ContractService;
import uz.idimzo.beck.utils.RestConstants;

import java.util.List;

@RestController
@RequestMapping(RestConstants.TM_BASE_URL + "/contracts")
@RequiredArgsConstructor
public class ContractController {

    private final ContractService contractService;

    @PostMapping("/templates")
    public ResponseEntity<ContractTemplateResponse> createContractTemplate(@Valid @RequestBody ContractTemplateRequest request) {
        ContractTemplateResponse template = contractService.createContractTemplate(request);
        return ResponseEntity.ok(template);
    }

    @GetMapping("/templates/category/{categoryId}")
    public ResponseEntity<List<ContractTemplateResponse>> getContractTemplatesByCategory(@PathVariable Long categoryId) {
        List<ContractTemplateResponse> templates = contractService.getContractTemplatesByCategory(categoryId);
        return ResponseEntity.ok(templates);
    }

    @GetMapping("/templates/{id}")
    public ResponseEntity<ContractTemplateResponse> getContractTemplateWithDetails(@PathVariable Long id) {
        ContractTemplateResponse template = contractService.getContractTemplateWithDetails(id);
        return ResponseEntity.ok(template);
    }
}