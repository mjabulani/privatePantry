package com.mjabulani.privatePantry.webclient;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsageDto {

    private int prompt_tokens;
    private int completion_tokens;
    private int total_tokens;

}
