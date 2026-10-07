package com.Esercitazione.ProductClient.client;

import com.Esercitazione.ProductClient.dto.ProductDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class ProductApiClient {

    private final RestClient restClient;

    public ProductApiClient(
            RestClient.Builder builder,
            @Value("${api.base-url}") String baseUrl) {

        this.restClient = builder
                .baseUrl(baseUrl)
                .build();
    }

    public List<ProductDTO> getAllProducts() {

        return restClient.get()
                .uri("/products")
                .retrieve()
                .body(
                    new ParameterizedTypeReference<List<ProductDTO>>() {}
                );
    }

    public ProductDTO getProductById(Long id) {

        return restClient.get()
                .uri("/products/{id}", id)
                .retrieve()
                .body(ProductDTO.class);
    }

    public List<ProductDTO> searchProducts(
            Long id,
            String name,
            String category,
            String priceOrder) {

        return restClient.get()
                .uri(uriBuilder -> {

                    uriBuilder.path("/products/search");

                    if (id != null) {
                        uriBuilder.queryParam("id", id);
                    }

                    if (name != null && !name.isBlank()) {
                        uriBuilder.queryParam("name", name);
                    }

                    if (category != null && !category.isBlank()) {
                        uriBuilder.queryParam("category", category);
                    }

                    if (priceOrder != null && !priceOrder.isBlank()) {
                        uriBuilder.queryParam(
                                "priceOrder",
                                priceOrder
                        );
                    }

                    return uriBuilder.build();
                })
                .retrieve()
                .body(
                    new ParameterizedTypeReference<List<ProductDTO>>() {}
                );
    }
}