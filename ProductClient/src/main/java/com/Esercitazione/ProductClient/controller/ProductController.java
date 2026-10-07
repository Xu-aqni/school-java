package com.Esercitazione.ProductClient.controller;

import com.Esercitazione.ProductClient.client.ProductApiClient;
import com.Esercitazione.ProductClient.dto.ProductDTO;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductApiClient productApiClient;

    public ProductController(ProductApiClient productApiClient) {
        this.productApiClient = productApiClient;
    }

    // http://localhost:8082/products
    @GetMapping
    public String products(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String priceOrder,
            Model model) {

        List<ProductDTO> products;

        if ((name == null || name.isBlank())
                && (category == null || category.isBlank())
                && (priceOrder == null || priceOrder.isBlank())) {

            products = productApiClient.getAllProducts();

        } else {

            products = productApiClient.searchProducts(
                    null,
                    name,
                    category,
                    priceOrder);
        }

        model.addAttribute("products", products);
        model.addAttribute("name", name);
        model.addAttribute("category", category);
        model.addAttribute("priceOrder", priceOrder);

        return "products";
    }

    // http://localhost:8082/products/1
    @GetMapping("/{id}")
    public String productDetails(
            @PathVariable Long id,
            Model model) {

        ProductDTO product = productApiClient.getProductById(id);

        model.addAttribute("product", product);

        return "product-detail";
    }
}