package uz.idimzo.beck.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.idimzo.beck.dto.contract.ContractTranslationDto;
import uz.idimzo.beck.dto.contract.*;
import uz.idimzo.beck.exception.ResourceNotFoundException;
import uz.idimzo.beck.mapper.ContractMapper;
import uz.idimzo.beck.entity.*;
import uz.idimzo.beck.repository.CategoryRepository;
import uz.idimzo.beck.repository.ContractRepository;
import uz.idimzo.beck.repository.ContractTemplateRepository;
import uz.idimzo.beck.repository.ContractTranslationRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContractService {

    private final ContractTemplateRepository contractTemplateRepository;
    private final CategoryRepository categoryRepository;
    private final ContractTranslationRepository contractTranslationRepository;
    private final ContractMapper contractMapper;
    private final UserService userService;
    private final ContractRepository contractRepository;

    @Transactional
    public ContractTemplateResponse createContractTemplate(ContractTemplateRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Kategoriya topilmadi"));

        ContractTemplate template = ContractTemplate.builder()
                .name(request.getName())
                .category(category)
                .translations(new ArrayList<>())
                .build();

        // Har bir til uchun tarjima va bo'limlarni yaratish
        for (ContractTranslationRequest translationRequest : request.getTranslations()) {
            ContractTranslation translation = ContractTranslation.builder()
                    .languageCode(translationRequest.getLanguageCode())
                    .content(translationRequest.getContent())
                    .template(template)
                    .sections(new ArrayList<>())
                    .build();

            // Bo'limlar va maydonlarni yaratish
            for (ContractSectionRequest sectionRequest : translationRequest.getSections()) {
                ContractSection section = createSection(sectionRequest, translation);
                translation.getSections().add(section);
            }

            template.getTranslations().add(translation);
        }


        template = contractTemplateRepository.save(template);
        return contractMapper.toTemplateResponse(template);
    }

    private ContractSection createSection(ContractSectionRequest request, ContractTranslation translation) {
        ContractSection section = ContractSection.builder()
                .name(request.getName())
                .orderIndex(request.getOrderIndex())
                .translation(translation)
                .fields(new ArrayList<>())
                .build();
        if (request.getFields() != null) {
            for (ContractFieldRequest fieldRequest : request.getFields()) {
                fieldRequest.validateFieldType();
                ContractField field = createField(fieldRequest, section);
                section.getFields().add(field);
            }
        }

        return section;
    }

    private ContractField createField(ContractFieldRequest request, ContractSection section) {
        ContractField field = ContractField.builder()
                .name(request.getName())
                .fieldId(request.getFieldId())
                .fieldType(request.getFieldType())
                .required(request.isRequired())
                .minValue(request.getMinValue())
                .maxValue(request.getMaxValue())
                .minLength(request.getMinLength())
                .maxLength(request.getMaxLength())
                .pattern(request.getPattern())
                .options(request.getOptions())
                .orderIndex(request.getOrderIndex())
                .placeholder(request.getPlaceholder())
                .helpText(request.getHelpText())
                .section(section)
                .build();

        // Agar fieldId berilmagan bo'lsa, avtomatik generatsiya qilinadi
        if (field.getFieldId() == null) {
            field.generateFieldId();
        }

        return field;
    }


    @Transactional(readOnly = true)
    public List<ContractTemplateResponse> getContractTemplatesByCategory(Long categoryId) {
        return contractTemplateRepository.findByCategoryId(categoryId).stream()
                .map(contractMapper::toTemplateResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ContractTemplateResponse getContractTemplateWithDetails(Long id) {
        Optional<ContractTemplate> template = contractTemplateRepository.findById(id);
        if (template.isEmpty()) {
            throw new IllegalArgumentException("Shartnoma shabloni topilmadi");
        }
        List<ContractTranslation> translations = contractTranslationRepository.findByTemplateId(id);
        template.get().setTranslations(translations);
        return contractMapper.toTemplateResponse(template.get());
    }

    public ContractTemplate getContractTemplateWithDetailsEntity(Long id) {
        ContractTemplate template = contractTemplateRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Shartnoma shabloni topilmadi"));

        List<ContractTranslation> translations = contractTranslationRepository.findByTemplateId(id);

        // Eski listni tozalab, yangilarni qo‘shamiz
        template.getTranslations().clear();
        for (ContractTranslation t : translations) {
            t.setTemplate(template); // MUHIM: aloqani tiklash
            template.getTranslations().add(t);
        }

        return template;
    }
    @Transactional
    public ContractFillResponse fillContract(Long templateId, String languageCode, ContractFillRequest request) {
        User currentUser = userService.getCurrentUser();

        ContractTemplate template = getContractTemplateWithDetailsEntity(templateId);

        ContractTranslation translation = template.getTranslations().stream()
                .filter(t -> t.getLanguageCode().equals(languageCode))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Ushbu tildagi shartnoma mavjud emas"));

        // Barcha maydonlarni tekshirish
        translation.getSections().forEach(section -> {
            section.getFields().forEach(field -> {
                String value = request.getFieldValues().get(field.getId().toString());
                if (field.isRequired() && (value == null || value.trim().isEmpty())) {
                    throw new IllegalArgumentException("Majburiy maydon to'ldirilmagan: " + field.getName());
                }
                // Qiymatni validatsiya qilish
                if (value != null && !value.trim().isEmpty()) {
                    field.validateValue(value);
                }
            });
        });

        String content = translation.getContent();
        Map<String, String> fieldValues = request.getFieldValues();

        // Maydon qiymatlarini joylashtirish
        for (Map.Entry<String, String> entry : fieldValues.entrySet()) {
            String fieldId = entry.getKey();
            String value = entry.getValue();
            if (value != null) {
                content = content.replaceAll("\\{#" + fieldId + "\\}", value);
            }
        }
        Contract contract = new Contract();
        contract.setContractTemplate(template);
        contract.setUser(currentUser);
        contract.setLanguageCode(languageCode);
        contract.setContent(content);
        contractRepository.save(contract);
        return ContractFillResponse.builder()
                .templateId(templateId)
                .languageCode(languageCode)
                .filledContent(content)
                .templateName(template.getName())
                .build();
    }
    @Transactional(readOnly = true)
    public ContractFillResponse getContractById(Long id) {
        User currentUser = userService.getCurrentUser();

        // In a real implementation, you would check if the contract belongs to the current user
        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contract not found with id: " + id));

        return ContractFillResponse.builder()
                .templateId(contract.getContractTemplate().getId())
                .languageCode(contract.getLanguageCode())
                .filledContent(contract.getContent())
                .templateName(contract.getContractTemplate().getName())
                .build();
    }
    @Transactional(readOnly = true)
    public List<ContractFillResponse> getUserContracts() {
        List<ContractFillResponse> responses = new ArrayList<>();
        User currentUser = userService.getCurrentUser();
        List<Contract> byUserId = contractRepository.findByUserId(currentUser.getId());
        for (Contract contract : byUserId) {

            ContractFillResponse fillResponse = new ContractFillResponse();
                    fillResponse.setTemplateId(contract.getContractTemplate().getId());
            fillResponse.setLanguageCode(contract.getLanguageCode());
            fillResponse.setFilledContent(contract.getContent());
            fillResponse.setTemplateName(contract.getContractTemplate().getName());
            responses.add(fillResponse);
        }
        return responses;
    }

    /**
     * Convert entity to DTO
     */
    private ContractTranslationDto convertToDto(ContractTranslation contract) {
        return ContractTranslationDto.builder()
                .id(contract.getId())
                .languageCode(contract.getLanguageCode())
                .content(contract.getContent())
                .templateId(contract.getTemplate().getId())
                .templateName(contract.getTemplate().getName())
                .createdAt(contract.getCreatedAt())
                .updatedAt(contract.getUpdatedAt())
                .build();
    }

}