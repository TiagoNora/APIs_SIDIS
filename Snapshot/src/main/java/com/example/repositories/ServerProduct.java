package com.example.repositories;
import com.example.model.Product;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ServerProduct {
    @Autowired
    ProductRepository repository;

    @RabbitListener(queues = "#{productQueue.name}")
    public List<Product> findProduct(int n) {
        return repository.findAll();
    }


}
