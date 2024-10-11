package com.microservices.products.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductsController {

    @GetMapping("/product")
    ResponseEntity<String> getProducts() {
        return ResponseEntity.ok("Hello products");
    }
}
