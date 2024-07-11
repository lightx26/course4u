package com.mgmtp.cfu.service;

import com.mgmtp.cfu.entity.Category;

import java.util.List;
import java.util.Set;

public interface CategoryService {
    List<String> getAllCategories();
    Set<Category> findCategoriesByNames(List<String> categoryNames);
}
