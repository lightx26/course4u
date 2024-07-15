package com.mgmtp.cfu.service.impl;

import com.mgmtp.cfu.dto.categorydto.CategoryDTO;
import com.mgmtp.cfu.entity.Category;
import com.mgmtp.cfu.enums.CategoryStatus;
import com.mgmtp.cfu.exception.MapperNotFoundException;
import com.mgmtp.cfu.mapper.DTOMapper;
import com.mgmtp.cfu.mapper.factory.impl.CategoryMapperFactory;
import com.mgmtp.cfu.repository.CategoryRepository;
import com.mgmtp.cfu.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapperFactory categoryMapperFactory;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapperFactory categoryMapperFactory) {
        this.categoryRepository = categoryRepository;
        this.categoryMapperFactory = categoryMapperFactory;
    }

    @Override
    public List<CategoryDTO> getAvailableCategories() {
        Optional<DTOMapper<CategoryDTO, Category>> mapperOpt = categoryMapperFactory.getDTOMapper(CategoryDTO.class);

        if (mapperOpt.isEmpty()) {
            throw new MapperNotFoundException("No mapper found for CategoryDTO");
        }

        DTOMapper<CategoryDTO, Category> mapper = mapperOpt.get();

        return categoryRepository.findCategoriesByStatus(CategoryStatus.AVAILABLE)
                .stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<String> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(Category::getName) // Assuming Category has a getName method
                .collect(Collectors.toList());
    }

    @Override
    public Set<Category> findCategoriesByNames(List<String> categoryNames) {
        return categoryRepository.findByNameIn(categoryNames);
    }

}
