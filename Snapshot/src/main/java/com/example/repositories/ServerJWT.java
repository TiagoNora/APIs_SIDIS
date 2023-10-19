package com.example.repositories;
import com.example.model.JWT;
import com.example.model.ProductDetailsDTO;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ServerJWT {
    @Autowired
    JWTRepository repository;

    @RabbitListener(queues = "#{jwtQueue.name}")
    public List<JWT> findProduct(int n) {
        return repository.findAll();
    }


}
