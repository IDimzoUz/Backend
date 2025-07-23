package uz.idimzo.beck.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.idimzo.beck.dto.contract.ContractTemplateDto;
import uz.idimzo.beck.entity.ContractTemplate;
import uz.idimzo.beck.exception.ResourceNotFoundException;
import uz.idimzo.beck.repository.ContractTemplateRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContractTemplateService {
    
    private final ContractTemplateRepository contractTemplateRepository;
    
    public List<ContractTemplateDto> getTemplatesByCategory(Long categoryId) {
        List<ContractTemplate> templates = contractTemplateRepository.findByCategoryId(categoryId);
        
        return templates.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public List<String> getAvailableLanguages(Long templateId) {
        if (!contractTemplateRepository.existsById(templateId)) {
            throw new ResourceNotFoundException("Template not found with id: " + templateId);
        }
        
        return contractTemplateRepository.findAvailableLanguagesByTemplateId(templateId);
    }
    
    private ContractTemplateDto convertToDto(ContractTemplate template) {
        List<String> availableLanguages = contractTemplateRepository
                .findAvailableLanguagesByTemplateId(template.getId());
        
        return ContractTemplateDto.builder()
                .id(template.getId())
                .name(template.getName())
                .availableLanguages(availableLanguages)
                .build();
    }
}