package com.alex.reactiveclient.controller;

import com.alex.reactiveclient.domain.Item;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class ItemClientController {

    WebClient webClient = WebClient.create("http://localhost:8080");

    @GetMapping("/client/retrieve")
    public Flux<Item> getAllItems() {
        return webClient.get().uri("/v1/items")
                .retrieve()
                .bodyToFlux(Item.class)
                .log();
    }

    @GetMapping("/client/exchange")
    public Flux<Item> getAllItemsExchange() {
        return webClient.get().uri("/v1/items")
                .exchange()
                .flatMapMany(clientResponse -> clientResponse.bodyToFlux(Item.class))
                .log();
    }

    @GetMapping("/client/retrieve/singleItem")
    public Mono<Item> getSingleItem() {
        return webClient.get().uri("/v1/items/{id}", "ABC")
                .retrieve()
                .bodyToMono(Item.class)
                .log();
    }

    @PostMapping("/client/createItem")
    public Mono<Item> createItem(@RequestBody Item item) {
        return webClient.post().uri("/v1/items")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(item), Item.class)
                .retrieve()
                .bodyToMono(Item.class)
                .log();
    }

}
