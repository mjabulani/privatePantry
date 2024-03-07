package com.mjabulani.privatePantry.model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductUpdate {


    private String name;
    private ProductCategory category;
    private Amount amount;

    public ProductUpdate(String name, ProductCategory category, Amount amount) {
        this.name = name;
        this.category = category;
        this.amount = amount;
    }
}
