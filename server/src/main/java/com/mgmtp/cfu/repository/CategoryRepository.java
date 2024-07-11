package com.mgmtp.cfu.repository;

import com.mgmtp.cfu.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByName(String name);

    Set<Category> findByNameIn(List<String> categoryNames);
}
