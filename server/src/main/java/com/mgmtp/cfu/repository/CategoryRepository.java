package com.mgmtp.cfu.repository;

import com.mgmtp.cfu.dto.categorydto.CategoryDTO;
import com.mgmtp.cfu.entity.Category;
import com.mgmtp.cfu.enums.CategoryStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>, JpaSpecificationExecutor<Category> {
    List<Category> findCategoriesByStatus(CategoryStatus status);
    List<Category> findByIdIn(Collection<Long> id);

    Optional<Category> findCategoryByNameIgnoreCase(String label);

    @Query("select new com.mgmtp.cfu.dto.categorydto.CategoryDTO(ca.id,ca.name) from Category ca join ca.courses co where co.id=:courseId")
    Set<CategoryDTO> findAllByCourseId(Long courseId);
}
