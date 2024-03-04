package com.mjabulani.privatePantry.webclient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class GptResponse {

    private String id;
    private String object;
    private double created;
    private String model;
    private List<ChoicesDto> choices;
    private UsageDto usage;
    private String system_fingerprint;

    public GptResponse() {

    }
}
