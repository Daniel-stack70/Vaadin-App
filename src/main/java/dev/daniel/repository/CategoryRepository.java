package dev.daniel.repository;

import dev.daniel.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long>, JpaSpecificationExecutor<Category> {

    List<Category> findByNameContainingIgnoreCase(String filterText);

    String countByNameContainingIgnoreCase(String filter);
}
