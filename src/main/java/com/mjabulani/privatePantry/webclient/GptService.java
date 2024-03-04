package com.mjabulani.privatePantry.webclient;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class GptService {

    private static final String uri = "api.openai.com/v1/chat/completions";
    private static final String token = "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";
    private final WebClient webClient;

    public GptService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(uri).build();
    }

    public Mono<GptResponse> calculateRecipe(GptRequestBody requestBody) {

        return webClient.post()
                .uri(uri)
                .bodyValue(requestBody)
                //.header("Authorization", token)
                .headers(httpHeaders -> httpHeaders.setBearerAuth(token))
                .exchange()
                .flatMap(clientResponse -> clientResponse.bodyToMono(GptResponse.class));
    }
}

