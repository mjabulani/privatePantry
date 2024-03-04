package com.mjabulani.privatePantry.api;

import com.mjabulani.privatePantry.model.*;
import com.mjabulani.privatePantry.repository.ProductRepository;
import com.mjabulani.privatePantry.webclient.GptRequestBody;
import com.mjabulani.privatePantry.webclient.GptResponse;
import com.mjabulani.privatePantry.webclient.GptService;
import com.mjabulani.privatePantry.webclient.Message;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;


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
    ProductCategory[] getProductCategories() {
        return ProductCategory.values();
    }

    // Add new product to database
    @PostMapping(
            value="products",
            produces="application/json")
    ProductEntity addProduct(@RequestBody ProductAddRequest product) { ;
        ProductEntity p = new ProductEntity(0, product.getName(), product.getCategory(), product.getAmount());
        productRepository.save(p);
        return p;
    }

    // Delete product by id
    @DeleteMapping(
            value="products/{id}",
            produces="application/json")
    void deleteProductById(@PathVariable int id) {
            productRepository.deleteById(id);
    }

    @PutMapping(
            value="products/{id}",
            produces="application/json")
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
    Mono<ResponseEntity<?>> calculateRecipe() {
        GptRequestBody requestBody = new GptRequestBody();
        List<Message> messages = new ArrayList<>();

        messages.add(new Message("system", "Act as a cook. You will be asked for recipes based on list of ingredients from my pantry. Please write 3 ideas for the meal."));
        messages.add(new Message("user", "Pasta spaghetti 300g, soya sauce 100ml, 5 eggs, bread, chicken breast 400g, salt, pepper"));
        requestBody.setModel("gpt-3.5-turbo");
        requestBody.setTemperature(0.4f);
        requestBody.setMax_tokens(64);
        requestBody.setTop_p(1);
        requestBody.setMessages(messages);
        return gptService.calculateRecipe(requestBody)
                .map(gptResponseResponseEntity -> {
                    if (gptResponseResponseEntity.getStatusCode().is2xxSuccessful()) {
                        return ResponseEntity.ok(gptResponseResponseEntity.getBody());
                    }  else {
                        return ResponseEntity.status(gptResponseResponseEntity.getStatusCode())
                                .body("Failed to create employee");
                    }
                })
                .onErrorResume(exception -> {
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body("Internal Cycki Error: " + exception.getMessage()));
                });
    }
}

