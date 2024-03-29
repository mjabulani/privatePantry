package com.mjabulani.privatePantry.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "products")
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "category")
    @Enumerated(EnumType.STRING)
    private ProductCategory category;

    @Column(name = "amount")
    private int amount;

    @Column(name = "unit")
    @Enumerated(EnumType.STRING)
    private ProductAmountUnit unit;

    public ProductEntity() {
    }

    public ProductEntity(int id, String name, ProductCategory category, int amount, ProductAmountUnit unit) {

        this.id = id;
        this.name = name;
        this.category = category;
        this.amount = amount;
        this.unit = unit;
    }

    public ProductEntity(String name, ProductCategory category, int amount, ProductAmountUnit unit) {
    }
}

