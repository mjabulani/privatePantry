package com.mjabulani.privatePantry.webclient;

import lombok.Getter;

@Getter
public class ChoicesDto {

    private int index;
    private Message message;
    private String logprobs;
    private String finish_reason;

}
