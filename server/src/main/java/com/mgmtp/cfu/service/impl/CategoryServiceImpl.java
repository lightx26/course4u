package com.mgmtp.cfu.service.impl;

import com.mgmtp.cfu.repository.CategoryRepository;
import com.mgmtp.cfu.service.CategoryService;
import org.springframework.stereotype.Service;
import com.mgmtp.cfu.entity.Category;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

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
