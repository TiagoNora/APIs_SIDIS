package com.example.controller;

import com.example.model.ProductDetailsDTO;
import com.example.services.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Tag(name = "Products", description = "Endpoints for getting information about the products")
@RequestMapping("/products")
@RestController
public class ProductController {

    @Autowired
    ProductService service;

    @Operation(summary = "Creates a product")
    @PostMapping(value = "/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDetailsDTO addProduct(@RequestBody ProductDetailsDTO details, HttpServletRequest request) throws JsonProcessingException {
        return service.createProduct(details,request);
    }



}
