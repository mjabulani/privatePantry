package com.mjabulani.privatePantry.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Amount {

    private int quantity;
    private ProductAmountUnit unit;

    public Amount(int quantity, ProductAmountUnit unit) {
        this.quantity = quantity;
        this.unit = unit;
    }

    public Amount() {

    }
}
