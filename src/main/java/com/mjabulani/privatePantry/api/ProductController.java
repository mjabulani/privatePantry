package com.mjabulani.privatePantry.api;

import com.mjabulani.privatePantry.model.*;
import com.mjabulani.privatePantry.repository.ProductRepository;
import com.mjabulani.privatePantry.webclient.GptRequestBody;
import com.mjabulani.privatePantry.webclient.GptResponse;
import com.mjabulani.privatePantry.webclient.GptService;
import com.mjabulani.privatePantry.webclient.Message;
;;;;;;import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;


@RestController
class ProductController {

    private final ProductRepository productRepository;
    private ProductCategory productCategory;
    public final GptService gptService;
    private GptResponse gptResponse;


    ProductController(GptService gptService, ProductRepository productRepository, GptService gptService1) {
        this.productRepository = productRepository;
        this.gptService = gptService1;
    }

    // Get name of product by id
    @GetMapping(
            value="products/name/{id}",
            produces="application/json")
    String getProductName(@PathVariable int id) {
        List<ProductEntity> p = productRepository.findById(id);
        return p.get(0).getName();

    }

    // Get product details by id
    @GetMapping(
            value="products/{id}",
            produces="application/json ;charset=UTF-8")
    ProductEntity getProductById(@PathVariable int id) {
        return productRepository.findById(id).get(0);
    }

    // Get list of products
    @GetMapping(
            value="products",
            produces="application/json")
    @CrossOrigin(origins = "*")
    List<ProductEntity> getAllProducts() {
        List<ProductEntity> products = new ArrayList<>();
        products.addAll(productRepository.findAll());
        return products;
    }

    // Get list of categories
    @GetMapping(
            value="products/categories",
            produces="application/json")
    @CrossOrigin(origins = "*")
    ProductCategory[] getProductCategories() {
        return ProductCategory.values();
    }

    // Add new product to database
    @PostMapping(
            value="products",
            produces="application/json")
    @CrossOrigin(origins = "*")
    ProductEntity addProduct(@RequestBody ProductAddRequest product) { ;
        ProductEntity p = new ProductEntity(0, product.getName(), product.getCategory(), product.getAmount());
        productRepository.save(p);
        return p;
    }

    // Delete product by id
    @DeleteMapping(
            value="products/{id}",
            produces="application/json")
    @CrossOrigin(origins = "*")
    void deleteProductById(@PathVariable int id) {
            productRepository.deleteById(id);
    }

    @PutMapping(
            value="products/{id}",
            produces="application/json")
    @CrossOrigin(origins = "*")
    ProductEntity updateProduct(@PathVariable int id, @RequestBody ProductUpdate product) {
        ProductEntity productToUpdate = productRepository.findById(id).get(0);
        productToUpdate.setAmount(product.getAmount());
        productToUpdate.setCategory(product.getCategory());
        productToUpdate.setAmount(product.getAmount());
        productRepository.save(productToUpdate);

        return productToUpdate;
    }

    @PostMapping(
            value="products/recipe",
            produces="application/json")
    @CrossOrigin(origins = "*")
    Mono<ResponseEntity<?>> calculateRecipe(@RequestBody RecipeRequestDto request) {
        GptRequestBody requestBody = new GptRequestBody();
        List<Message> messages = new ArrayList<>();
        String ingredientsString = getIngredients(request);
        String type = getTypeOfRecipe(request);

        messages.add(new Message("system", "Zachowuj się jak szef kuchni. \n" +
                "Podam Ci listę składników, które znajdują się w mojej spiżarni\n" +
                "Zaproponowane danie musi być na " + type + ". W przepisie nie muszą być wykorzystane wszystkie składniki oraz ich cała ilość. Posiłek ma być najbardziej optymalny. Do dania użyj jedynie wskazanych produktów, innych nie dodawaj, jeżeli ich brakuje."));
        messages.add(new Message("user", ingredientsString));
        requestBody.setModel("gpt-3.5-turbo");
        requestBody.setTemperature(request.getSearchParameters().getTemperature());
        requestBody.setMax_tokens(request.getSearchParameters().getMax_tokens());
        requestBody.setTop_p(request.getSearchParameters().getTop_p());
        requestBody.setMessages(messages);
        return gptService.calculateRecipe(requestBody)
                .map(gptResponseResponseEntity -> {
                    if (gptResponseResponseEntity.getStatusCode().is2xxSuccessful()) {
                        return ResponseEntity.ok(gptResponseResponseEntity.getBody());
                    }  else {
                        return ResponseEntity.status(gptResponseResponseEntity.getStatusCode())
                                .body("Failed to call GPT engine");
                    }
                })
                .onErrorResume(exception -> {
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body("Internal error: " + exception.getMessage()));
                });
    }

    String getIngredients(RecipeRequestDto request) {
        ArrayList<String> ingredients = new ArrayList<>();
        for (int i = 0; i < request.getItems().size(); i++) {
            ingredients.add(request.getItems().get(i).getName() + " - " + request.getItems().get(i).getAmount());
        }
        StringJoiner stringJoiner = new StringJoiner(", ");
        for (String ingredient : ingredients) {
            stringJoiner.add(ingredient);
        }
        return stringJoiner.toString();

    }

    String getTypeOfRecipe(RecipeRequestDto request) {
        if (request.isSweet()) {
            return "słodko";
        } else {
            return "wytrawnie";
        }
    }
}

