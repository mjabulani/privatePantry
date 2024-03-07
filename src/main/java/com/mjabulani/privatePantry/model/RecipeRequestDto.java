package com.mjabulani.privatePantry.model;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class RecipeRequestDto {

    private List<RecipeItemDto> items;
    private boolean isSweet;
    private MealType mealtype;
    private int peopleCount;
    private SearchParametersDto searchParameters;
}
