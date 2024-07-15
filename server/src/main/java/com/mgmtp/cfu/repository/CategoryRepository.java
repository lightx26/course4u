package com.mgmtp.cfu.repository;

import com.mgmtp.cfu.entity.Category;
import com.mgmtp.cfu.enums.CategoryStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>, JpaSpecificationExecutor<Category> {
    List<Category> findCategoriesByStatus(CategoryStatus status);
    List<Category> findByIdIn(Collection<Long> id);
}
