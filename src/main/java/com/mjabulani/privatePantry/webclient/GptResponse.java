package com.mjabulani.privatePantry.webclient;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GptResponse {

    private String id;
    private String object;
    private double created;
    private String model;
    private ChoicesDto choicesDto;
    private UsageDto usage;
    private String system_fingerprint;

    public GptResponse(String id, String object, double created, String model, ChoicesDto choicesDto, UsageDto usage, String system_fingerprint) {
        this.id = id;
        this.object = object;
        this.created = created;
        this.model = model;
        this.choicesDto = choicesDto;
        this.usage = usage;
        this.system_fingerprint = system_fingerprint;
    }

    public GptResponse() {

    }
}
