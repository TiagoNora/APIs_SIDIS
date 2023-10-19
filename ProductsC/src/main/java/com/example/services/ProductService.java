package com.example.services;

import com.example.model.Product;
import com.example.model.ProductDTO;
import com.example.model.ProductDetailsDTO;
import com.example.model.UserDetailsDTO;
import com.example.repositories.ProductRepository;
import com.example.repositories.RabbitMQSender;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {
    @Autowired
    ProductRepository repository;
    @Autowired
    RequestService service;

    @Autowired
    RabbitMQSender rabbitMQSender;

    public List<ProductDTO> findAllProductsFromCatalog(Integer pageNo, Integer pageSize) {
        Pageable paging = PageRequest.of(pageNo, pageSize);
        Page<ProductDTO> pagedResult = repository.findAllProductsFromCatalog(paging);
        List<ProductDTO> products = new ArrayList<>();
        if(pagedResult != null && pagedResult.hasContent()) {
            products = pagedResult.getContent();
        }
        return products;
    }
    public ProductDetailsDTO findProductBySku(String sku) throws IOException {
        ProductDetailsDTO product = repository.findProductBySku(sku);
        if (product == null){
            product=service.retrieveProductFromApi(sku);
            if (product == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product Not Found ");
            }
        }
        return product;
    }

    public List<ProductDTO> findByNameOrSku(String nameOrSku,Integer pageNo,Integer pageSize){
        Pageable paging = PageRequest.of(pageNo, pageSize);
        Page<ProductDTO> productPage = repository.findByNameOrSku(nameOrSku,paging);
        List<ProductDTO> products = new ArrayList<>();
        if(productPage != null && productPage.hasContent()) {
            products = productPage.getContent();
        }
        return products;
    }

    public ProductDetailsDTO createProduct(ProductDetailsDTO details, HttpServletRequest request) throws JsonProcessingException {
        String jwt = service.parseJwt(request);
        UserDetailsDTO user = service.makeRequestToAutentication(jwt);

        /*if (!user.getRoles().equals("[MODERATOR]")){
            System.out.println(user.getRoles());
            throw  new ResponseStatusException(HttpStatus.FORBIDDEN, "Can´t be accessed by this user");
        }*/
        Product product = new Product(details.getSku(), details.getName(), details.getDescription());

        repository.save(product);
        rabbitMQSender.sendJsonMessage(product);

        ProductDetailsDTO detailsDTO = new ProductDetailsDTO(product);
        return detailsDTO;
    }

    public void createProduct(Product product) throws JsonProcessingException {
        ProductDetailsDTO productDetailsDTO = repository.findProductBySku(product.getSku());
        System.out.println(productDetailsDTO);
        if (productDetailsDTO == null) repository.save(product);
    }
}
