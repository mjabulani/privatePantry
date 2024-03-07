package com.mjabulani.privatePantry.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchParametersDto {

    private float temperature;
    private int max_tokens;
    private float top_p;
}
