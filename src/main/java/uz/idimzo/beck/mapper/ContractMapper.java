package uz.idimzo.beck.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uz.idimzo.beck.dto.contract.ContractFieldResponse;
import uz.idimzo.beck.dto.contract.ContractSectionResponse;
import uz.idimzo.beck.dto.contract.ContractTemplateResponse;
import uz.idimzo.beck.dto.contract.ContractTranslationResponse;
import uz.idimzo.beck.entity.*;

@Mapper(componentModel = "spring")
public interface ContractMapper {
    
//    @Mapping(target = "categoryId", source = "category.id")
//    @Mapping(target = "categoryName", source = "category.name")
@Mapping(target = "translations", source = "translations")
ContractTemplateResponse toTemplateResponse(ContractTemplate template);
    
    ContractTranslationResponse toTranslationResponse(ContractTranslation translation);
    
    ContractSectionResponse toSectionResponse(ContractSection section);
    
    ContractFieldResponse toFieldResponse(ContractField field);
}