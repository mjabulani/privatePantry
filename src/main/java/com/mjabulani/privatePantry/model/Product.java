package com.mjabulani.privatePantry.model;

import jakarta.persistence.GeneratedValue;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

@Getter
@Setter
@Value
public class Product {

    @GeneratedValue
    private int id;

    private String name;
    private ProductCategory category;
    private Amount amount;

    public Product(int id, String name, ProductCategory category, Amount amount) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.amount = amount;
    }
}
