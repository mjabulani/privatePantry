package com.mjabulani.privatePantry.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
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
