package com.mjabulani.privatePantry.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class RecipeItemDto {

    private String name;
    private String amount;
}
