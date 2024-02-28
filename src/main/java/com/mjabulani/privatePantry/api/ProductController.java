package com.mjabulani.privatePantry.api;

import com.mjabulani.privatePantry.model.Product;
import com.mjabulani.privatePantry.model.ProductAddRequest;
import com.mjabulani.privatePantry.model.ProductCategory;
import com.mjabulani.privatePantry.model.ProductEntity;
import com.mjabulani.privatePantry.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.List;


@RestController
class ProductController {

    private final ProductRepository productRepository;
    private ProductCategory productCategory;

    ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // Get name of product by id
    @GetMapping(
            value="products/name/{id}",
            produces="application/json")
    String getProductName(@PathVariable int id) {
        List<ProductEntity> p = productRepository.findById(id);
        return p.get(0).getName();

    }

    // Get product details by id
    @GetMapping(
            value="products/{id}",
            produces="application/json ;charset=UTF-8")
    ProductEntity getProductById(@PathVariable int id) {
        return productRepository.findById(id).get(0);
    }

    // Get list of products
    @GetMapping(
            value="products",
            produces="application/json")
    List<ProductEntity> getAllProducts() {
        List<ProductEntity> products = new ArrayList<>();
        products.addAll(productRepository.findAll());
        return  products;
    }

    // Get list of categories
    @GetMapping(
            value="products/categories",
            produces="application/json")
    ProductCategory[] getProductCategories() {
        return ProductCategory.values();
    }

    // Add new product to database
    @PostMapping(
            value="products",
            produces="application/json")
    ProductEntity addProduct(@RequestBody ProductAddRequest product) { ;
        ProductEntity p = new ProductEntity(0, product.getName(), product.getCategory(), product.getAmount());
        productRepository.save(p);
        return p;
    }

    // Delete product by id
    @DeleteMapping(
            value="products/{id}",
            produces="application/json")
    void deleteProductById(@PathVariable int id) {
            productRepository.deleteById(id);
    }
}

