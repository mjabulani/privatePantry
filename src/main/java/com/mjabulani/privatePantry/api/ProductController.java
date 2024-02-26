package com.mjabulani.privatePantry.api;

import com.mjabulani.privatePantry.model.Product;
import com.mjabulani.privatePantry.model.ProductCategory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class ProductController {

    @GetMapping(
            value="products",
            produces="application/json")
    Product getProductById() {
        return new Product(50, "Lol", ProductCategory.CHEESE);
    }

}
