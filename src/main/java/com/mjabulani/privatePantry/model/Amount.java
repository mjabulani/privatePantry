package com.mjabulani.privatePantry.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Amount {

    private int count;
    private ProductAmountUnit unit;

    public Amount(int count, ProductAmountUnit unit) {
        this.count = count;
        this.unit = unit;
    }

    public Amount() {

    }
}
