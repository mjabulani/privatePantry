package com.mjabulani.privatePantry.api;

import com.fasterxml.jackson.databind.cfg.MapperBuilder;
import com.mjabulani.privatePantry.model.*;
import com.mjabulani.privatePantry.repository.ProductRepository;
import com.mjabulani.privatePantry.webclient.GptRequestBody;
import com.mjabulani.privatePantry.webclient.GptResponse;
import com.mjabulani.privatePantry.webclient.GptService;
import com.mjabulani.privatePantry.webclient.Message;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.*;


@RestController
class ProductController {

    private final ProductRepository productRepository;
    private ProductCategory productCategory;
    public final GptService gptService;
    private GptResponse gptResponse;
    private ProductAmountUnit productAmountUnit;

    ProductController(GptService gptService, ProductRepository productRepository) {
        this.productRepository = productRepository;
        this.gptService = gptService;
    }

    // Get name of product by id
    @GetMapping(
            value = "products/name/{id}",
            produces = "application/json")
    String getProductName(@PathVariable int id) {
        List<ProductEntity> p = productRepository.findById(id);
        return p.get(0).getName();

    }

    // Get product details by id
    @GetMapping(
            value = "products/{id}",
            produces = "application/json; charset=UTF-8")
    Product getProductById(@PathVariable int id) {
        ProductEntity p = productRepository.findById(id).get(0);
        Amount amount = new Amount(p.getAmount(), p.getUnit());
        return new Product(
                p.getId(),
                p.getName(),
                p.getCategory(),
                amount);
    }


    // Get list of products
    @GetMapping(
            value = "products",
            produces = "application/json")
    @CrossOrigin(origins = "*")
    List<Product> getAllProducts() {
        List<Product> productList = new ArrayList<>();
        List<ProductEntity> products = productRepository.findAll();
        for (ProductEntity productEntity : products) {
            Amount amount = new Amount(
                    productEntity.getAmount(), productEntity.getUnit());

            productList.add(
                    Product.builder()
                            .id(productEntity.getId())
                            .name(productEntity.getName())
                            .category(productEntity.getCategory())
                            .amount(amount)
                            .build());
        }
        return productList;
    }

    // Get list of categories
    @GetMapping(
            value = "products/categories",
            produces = "application/json")
    @CrossOrigin(origins = "*")
    ProductCategory[] getProductCategories() {
        return ProductCategory.values();
    }

    // Add new product to database
    @PostMapping(
            value = "products",
            produces = "application/json")
    @CrossOrigin(origins = "*")
    ProductEntity addProduct(@RequestBody ProductAddRequest product) {
        ProductEntity p = new ProductEntity(0, product.getName(), product.getCategory(), product.getAmount().getCount(), product.getAmount().getUnit());
        productRepository.save(p);
        return p;
    }

    // Delete product by id
    @DeleteMapping(
            value = "products/{id}",
            produces = "application/json")
    @CrossOrigin(origins = "*")
    void deleteProductById(@PathVariable int id) {
        productRepository.deleteById(id);
    }


    // Update product by id
    @PutMapping(
            value = "products/{id}",
            produces = "application/json")
    @CrossOrigin(origins = "*")
    Product updateProduct(@PathVariable int id, @RequestBody ProductUpdate product) {
        ProductEntity productToUpdate = productRepository.findById(id).get(0);
        productToUpdate.setName(product.getName());
        productToUpdate.setCategory(product.getCategory());
        productToUpdate.setAmount(product.getAmount().getCount());
        productToUpdate.setUnit(product.getAmount().getUnit());
        productRepository.save(productToUpdate);
        Amount amount = new Amount(productToUpdate.getAmount(), productToUpdate.getUnit());
        return new Product(
                productToUpdate.getId(),
                productToUpdate.getName(),
                productToUpdate.getCategory(),
                amount);
    }

    @PostMapping(
            value = "products/recipe",
            produces = "application/json")
    @CrossOrigin(origins = "*")
    Mono<ResponseEntity<?>> calculateRecipe(@RequestBody RecipeRequestDto request) {
        GptRequestBody requestBody = new GptRequestBody();
        List<Message> messages = new ArrayList<>();
        String ingredientsString = getIngredients(request);
        String type = getTypeOfRecipe(request);

        messages.add(new Message("system",
                "Zachowuj się jak kucharz domowy, który tworzy " + getTypeOfRecipe(request) + peopleCount(request) +
                        " korzystając tylko z dostępnych składników w lodówce i spiżarni." +
                        " Postaraj się, aby danie było pełnowartościowe i wysokobiałkowe," +
                        " ograniczając cukry oraz węglowodany proste." +
                        " Po przygotowaniu dania, podaj makroskładniki całego posiłku oraz jego kaloryczność przedstawioną jako tabelę w tagach html (<table> oraz wiersze, kolumny)"));

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
                    } else {
                        return ResponseEntity.status(gptResponseResponseEntity.getStatusCode())
                                .body("Failed to call GPT engine");
                    }
                })
                .onErrorResume(exception -> {
                    return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Internal error: " + exception.getMessage()));
                });
    }

    String getIngredients(RecipeRequestDto request) {
        ArrayList<String> ingredients = new ArrayList<>();
        for (int i = 0; i < request.getItems().size(); i++) {
            ingredients.add(request.getItems().get(i).getName() + " - " + request.getItems().get(i).getAmount().getCount() + " " + request.getItems().get(0).getAmount().getUnit());
        }
        StringJoiner stringJoiner = new StringJoiner(", ");
        for (String ingredient : ingredients) {
            stringJoiner.add(ingredient);
        }
        return stringJoiner.toString();

    }

    String getTypeOfRecipe(RecipeRequestDto request) {
        if (request.isSweet()) {
            return "słodkie";
        } else {
            return "wytrawne";
        }
    }

    String peopleCount(RecipeRequestDto request) {
        if (request.getPeopleCount() == 1) {
            return "jednej osoby";
        } else {
            return request.getPeopleCount() + " osób";
        }
    }
}

