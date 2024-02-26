package com.mjabulani.privatePantry.model;


import lombok.Builder;
import lombok.Value;
import org.springframework.scheduling.annotation.Scheduled;


@Value
public class Product {

    private int id;
    private String name;

    public Product(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
