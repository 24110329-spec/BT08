package vn.iotstar.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.iotstar.entity.Category;

import java.util.List;
import java.util.Optional;

public interface ICategoryService {
    List<Category> findAll();
    Page<Category> findAll(Pageable pageable);
    Page<Category> findByCategoryNameContaining(String name, Pageable pageable);
    List<Category> findByCategoryNameContaining(String name);
    Optional<Category> findById(Long id);
    Optional<Category> findByCategoryName(String name);
    Category save(Category category);
    void deleteById(Long id);
}