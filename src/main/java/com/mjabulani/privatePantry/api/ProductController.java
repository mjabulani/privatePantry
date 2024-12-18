package com.mjabulani.privatePantry.api;

import com.fasterxml.jackson.databind.cfg.MapperBuilder;
import com.mjabulani.privatePantry.api.exception.ProductAlreadyExistsException;
import com.mjabulani.privatePantry.api.exception.ProductDoesNotExist;
import com.mjabulani.privatePantry.model.*;
import com.mjabulani.privatePantry.repository.ProductRepository;
import com.mjabulani.privatePantry.webclient.GptRequestBody;
import com.mjabulani.privatePantry.webclient.GptResponse;
import com.mjabulani.privatePantry.webclient.GptService;
import com.mjabulani.privatePantry.webclient.Message;
import jakarta.transaction.Transactional;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.yaml.snakeyaml.util.EnumUtils;
import reactor.core.publisher.Mono;

import java.util.*;

import org.slf4j.Logger;


@RestController
class ProductController {

    Logger logger = LoggerFactory.getLogger(ProductController.class);
    private final ProductRepository productRepository;
    private ProductCategory productCategory;
    public final GptService gptService;
    private GptResponse gptResponse;
    private ProductAmountUnit productAmountUnit;

    ProductController(GptService gptService, ProductRepository productRepository) {
        this.productRepository = productRepository;
        this.gptService = gptService;
    }

//    // Get name of product by id
//    @GetMapping(
//            value = "products/name/{id}",
//            produces = "application/json")
//    String getProductName(@PathVariable String id) {
//        List<ProductEntity> p = productRepository.findById(id);
//        return p.get(0).getName();
//    }

    // Get product details by id
    @GetMapping(
            value = "products/{id}",
            produces = "application/json; charset=UTF-8")
    Product getProductById(@PathVariable String id) {
        List<ProductEntity> productEntityList = productRepository.findById(id);
        if (!productEntityList.isEmpty()) {
            ProductEntity p = productEntityList.get(0);
            return Product.builder()
                    .id(p.getId())
                    .name(p.getName())
                    .category(p.getCategory())
                    .amount(Amount.builder()
                            .count(p.getAmount())
                            .unit(p.getUnit())
                            .build())
                    .build();
        } else {
            throw new ProductDoesNotExist("Product with id = " + id + " does not exist!");
        }
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
            productList.add(
                    Product.builder()
                            .id(productEntity.getId())
                            .name(productEntity.getName())
                            .category(productEntity.getCategory())
                            .amount(Amount.builder()
                                    .count(productEntity.getAmount())
                                    .unit(productEntity.getUnit())
                                    .build())
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

    // Get list of units
    @GetMapping(
            value = "products/units",
            produces = "application/json")
    @CrossOrigin(origins = "*")
    ProductAmountUnit[] getUnits() {
        return ProductAmountUnit.values();
    }

    // Add new product to database
    @PostMapping(
            value = "products",
            produces = "application/json")
    @CrossOrigin(origins = "*")
    Product addProduct(@RequestBody ProductAddRequest product) {
        if (productRepository.findByName(product.getName()) == null) {
            ProductEntity pe = productRepository.save(ProductEntity.builder()
                    .id(UUID.randomUUID().toString())
                    .name(product.getName())
                    .category(product.getCategory())
                    .amount(product.getAmount().getCount())
                    .unit(product.getAmount().getUnit())
                    .build());
            return Product.builder()
                    .id(pe.getId())
                    .name(pe.getName())
                    .category(pe.getCategory())
                    .amount(Amount.builder()
                            .count(pe.getAmount())
                            .unit(pe.getUnit())
                            .build())
                    .build();
        } else {
            throw new ProductAlreadyExistsException(product.getName() + " już istnieje");
        }
    }

    // Delete product by id
    @DeleteMapping(
            value = "products/{id}",
            produces = "application/json")
    @CrossOrigin(origins = "*")
    @Transactional
    void deleteProductById(@PathVariable String id) {
        if (!productRepository.findById(id).isEmpty()) {
            productRepository.deleteById(id);
        } else {
            throw new ProductDoesNotExist("Nie udało się usunąć produktu o id: " + id + ". Taki produkt nie istnieje!");
        }
    }


    // Update product by id
    @PutMapping(
            value = "products/{id}",
            produces = "application/json")
    @CrossOrigin(origins = "*")
    Product updateProduct(@PathVariable String id, @RequestBody ProductUpdate product) {
        List<ProductEntity> productEntityList = productRepository.findById(id);
        if (!productEntityList.isEmpty()) {
            ProductEntity productToUpdate = productEntityList.get(0);
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
        } else {
            throw new ProductDoesNotExist("Product with id: " + id + " does not exist.");
        }

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
                        " Jeżeli nie jesteś w stanie przygotować przepisu z podanych składników, wskaż co należy dokupić, by minimalnym kosztem uzyskać dobry poziłek. " +
                        " Postaraj się, aby danie było pełnowartościowe i wysokobiałkowe," +
                        " ograniczając cukry oraz węglowodany proste." +
                        " Po przygotowaniu dania, podaj makroskładniki całego posiłku oraz jego kaloryczność." +
                        " Całą odpowiedź przedstaw w tagach HTML, bym mógł osadzić ją na stronie internetowej w przystępnej formie. Użyj tagów html, a do określenia kaloryczności tabeli html."));

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
            ingredients.add(request.getItems()
                    .get(i).getName() + " - " +
                    request
                            .getItems()
                            .get(i)
                            .getAmount()
                            .getCount() + " " +
                    request
                            .getItems()
                            .get(0)
                            .getAmount()
                            .getUnit());
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

