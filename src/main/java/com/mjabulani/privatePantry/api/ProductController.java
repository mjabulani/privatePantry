package com.mjabulani.privatePantry.api;

import com.mjabulani.privatePantry.model.Product;
import com.mjabulani.privatePantry.model.ProductCategory;
import com.mjabulani.privatePantry.model.ProductEntity;
import com.mjabulani.privatePantry.repository.ProductRepository;
import org.apache.tomcat.util.http.parser.HttpParser;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Arrays;
import java.util.stream.Stream;


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
        ProductEntity p = productRepository.findById(id).get(0);
        return p;
    }

    @GetMapping(
            value="products",
            produces="application/json")
    Product getProductById() {
        return new Product(21, "Ryż", ProductCategory.CARBS, 123);
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

    ProductEntity addProduct(@RequestBody Product product) { ;
        return new ProductEntity(product.getId(), product.getName(), product.getCategory(), product.getAmount());
    }
}

