package com.mgmtp.cfu.controller;

import com.mgmtp.cfu.dto.categorydto.CategoryDTO;
import com.mgmtp.cfu.exception.MapperNotFoundException;
import com.mgmtp.cfu.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }


    @GetMapping("/available")
    public ResponseEntity<?> getAvailableCategories() {
        try {
            List<CategoryDTO> availableCategories = categoryService.getAvailableCategories();
            return ResponseEntity.ok(availableCategories);
        } catch (MapperNotFoundException e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }
}
