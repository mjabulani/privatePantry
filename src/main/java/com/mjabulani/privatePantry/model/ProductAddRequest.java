package com.mjabulani.privatePantry.model;

import jakarta.persistence.GeneratedValue;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductAddRequest {


    private String name;
    private ProductCategory category;
    private int amount;

    public ProductAddRequest(String name, ProductCategory category, int amount) {
        this.name = name;
        this.category = category;
        this.amount = amount;
    }

    public ProductAddRequest() {

    }
}

