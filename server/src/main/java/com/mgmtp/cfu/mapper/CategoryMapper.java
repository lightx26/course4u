package com.mgmtp.cfu.mapper;

import com.mgmtp.cfu.dto.categorydto.CategoryDTO;
import com.mgmtp.cfu.entity.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class CategoryMapper implements DTOMapper<CategoryDTO, Category>, EntityMapper<CategoryDTO, Category>{
    public abstract CategoryDTO toDTO(Category category);
    public abstract Category toEntity(CategoryDTO categoryDTO);
}
