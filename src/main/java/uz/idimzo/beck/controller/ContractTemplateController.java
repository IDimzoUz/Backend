package uz.idimzo.beck.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.idimzo.beck.dto.contract.ContractTemplateDto;
import uz.idimzo.beck.service.ContractTemplateService;
import uz.idimzo.beck.utils.RestConstants;

import java.util.List;

@RestController
@RequestMapping(RestConstants.TM_BASE_URL + "/contract-templates")
@RequiredArgsConstructor
public class ContractTemplateController {
    
    private final ContractTemplateService contractTemplateService;
    
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ContractTemplateDto>> getTemplatesByCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(contractTemplateService.getTemplatesByCategory(categoryId));
    }
    
    @GetMapping("/{templateId}/languages")
    public ResponseEntity<List<String>> getAvailableLanguages(@PathVariable Long templateId) {
        return ResponseEntity.ok(contractTemplateService.getAvailableLanguages(templateId));
    }
}