package com.mjabulani.privatePantry.repository;

import com.mjabulani.privatePantry.model.ProductCategory;
import com.mjabulani.privatePantry.model.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.webmvc.RepositoryRestController;

import java.util.List;

@RepositoryRestController(path="products")
public interface ProductRepository extends JpaRepository<ProductEntity, Integer> {
    List<ProductEntity> findById(int id);

    List<ProductEntity> findByCategory(ProductCategory category);

    ProductEntity findByName(String name);
}
