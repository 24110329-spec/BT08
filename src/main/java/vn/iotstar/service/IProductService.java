package vn.iotstar.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.iotstar.entity.Product;

import java.util.List;
import java.util.Optional;

public interface IProductService {
    List<Product> findAll();
    Page<Product> findAll(Pageable pageable);
    Page<Product> findByProductNameContaining(String name, Pageable pageable);
    List<Product> findByProductNameContaining(String name);
    Optional<Product> findById(Long id);
    Product save(Product product);
    void deleteById(Long id);
}