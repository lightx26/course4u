package com.mgmtp.cfu.service.impl;

import com.mgmtp.cfu.dto.categorydto.CategoryDTO;
import com.mgmtp.cfu.dto.coursedto.CourseRequest;
import com.mgmtp.cfu.entity.Category;
import com.mgmtp.cfu.enums.CategoryStatus;
import com.mgmtp.cfu.exception.MapperNotFoundException;
import com.mgmtp.cfu.mapper.DTOMapper;
import com.mgmtp.cfu.mapper.factory.impl.CategoryMapperFactory;
import com.mgmtp.cfu.repository.CategoryRepository;
import com.mgmtp.cfu.service.CategoryService;
import com.mgmtp.cfu.util.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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
    public List<Category> findOrCreateNewCategory(List<CourseRequest.CategoryCourseRequestDTO> categoryRequests) {
        List<Category> categories = new ArrayList<>();
        try {
            categoryRequests.forEach(categoryRequestDTO -> {
                String value = categoryRequestDTO.getValue();
                Optional<Category> existCategory;
                // Check if value is a number (ID)
                if (value.matches("\\d+")) {
                    existCategory = categoryRepository.findById(Long.valueOf(value));
                } else {
                    existCategory = categoryRepository.findCategoryByNameIgnoreCase(value);
                }
                if (existCategory.isPresent()) {
                    if (!categories.contains(existCategory.get())) {
                        categories.add(existCategory.get());
                    }
                } else {
                    if (!value.matches("\\d+")) { // Only create new category if value is not an ID
                        Category newCategory = new Category();
                        newCategory.setName(value);
                        CategoryStatus status = AuthUtils.getCurrentUser().getRole().toString().equals("ADMIN") ? CategoryStatus.AVAILABLE : CategoryStatus.PENDING;
                        newCategory.setStatus(status);
                        categories.add(categoryRepository.save(newCategory));
                    }
                }
            });
            return categories;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
