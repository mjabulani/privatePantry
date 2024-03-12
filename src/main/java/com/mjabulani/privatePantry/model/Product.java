package com.mjabulani.privatePantry.model;

import jakarta.persistence.GeneratedValue;
import lombok.*;

@Getter
@Setter
@Value
@Builder
@AllArgsConstructor
public class Product {

    @GeneratedValue
    private int id;

    private String name;
    private ProductCategory category;
    private Amount amount;


}
