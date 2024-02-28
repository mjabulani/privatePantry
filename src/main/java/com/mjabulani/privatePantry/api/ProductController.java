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


    @GetMapping(
            value="products/name",
            produces="application/json")
    String getProductName() {
        Product p = new Product(50, "Lol", ProductCategory.CHEESE, 5);
        return p.getName();
    }

    @GetMapping(
            value="products/{id}",
            produces="application/json ;charset=UTF-8")
    ProductEntity getProductById(@PathVariable int id) {
        return productRepository.findById(id).get(0);
    }

    @GetMapping(
            value="products",
            produces="application/json")
    List<ProductEntity> getAllProducts() {
        List<ProductEntity> products = new ArrayList<>();
        products.addAll(productRepository.findAll());

        return  products;
    }

    @GetMapping(
            value="products/categories",
            produces="application/json")
    ProductCategory[] getProductCategories() {
        return ProductCategory.values();
    }

    @PostMapping(
            value="products",
            produces="application/json")
    ProductEntity addProduct(@RequestBody ProductAddRequest product) { ;
        ProductEntity p = new ProductEntity(0, product.getName(), product.getCategory(), product.getAmount());
        productRepository.save(p);
        return p;
    }

    @DeleteMapping(
            value="products/{id}",
            produces="application/json")
    void deleteProductById(@PathVariable int id) {
            productRepository.deleteById(id);
    }
}

