package com.mjabulani.privatePantry.model;


import lombok.Builder;
import lombok.Value;
import org.springframework.scheduling.annotation.Scheduled;


@Value
public class Product {

    private int id;
    private String name;
    private ProductCategory category;

    public Product(int id, String name, ProductCategory category) {
        this.id = id;
        this.name = name;
        this.category = category;
    }
}
