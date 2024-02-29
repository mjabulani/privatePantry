package com.mjabulani.privatePantry.model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductUpdate {


    private String name;
    private ProductCategory category;
    private int amount;

    public ProductUpdate(String name, ProductCategory category, int amount) {
        this.name = name;
        this.category = category;
        this.amount = amount;
    }
}
