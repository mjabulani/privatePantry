package com.mjabulani.privatePantry.webclient;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class GptRequestBody {

    private String model;
    private List<Message> messages;
    private float temperature;
    private int max_tokens;
    private float top_p;


    public GptRequestBody(String model, List<Message> messages, float temperature, int max_tokens, float top_p) {
        this.model = model;
        this.messages = messages;
        this.temperature = temperature;
        this.max_tokens = max_tokens;
        this.top_p = top_p;
    }

    public GptRequestBody() {
        
    }
}
