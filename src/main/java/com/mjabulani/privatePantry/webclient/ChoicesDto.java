package com.mjabulani.privatePantry.webclient;

import lombok.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChoicesDto {

    private int index;
    private Message message;
    private String logprobs;
    private String finish_reason;

}
