package com.mjabulani.privatePantry.api;

import com.mjabulani.privatePantry.model.Product;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class ProductController {

    @GetMapping("/products")
    String getProduct() {
        return "hello World";
    }

    @GetMapping(
            value="products/{id}",
            produces="application/json")
    Product getProductById() {
        return new Product(50, "Lol");
    }

}
