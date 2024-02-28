package com.mjabulani.privatePantry.model;

import lombok.Getter;
import lombok.Setter;
import lombok.Value;

@Getter
@Setter
@Value
public class Product {

    private int id;
    private String name;
    private ProductCategory category;
    private int amount;

    public Product(int id, String name, ProductCategory category, int amount) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.amount = amount;
    }
}
