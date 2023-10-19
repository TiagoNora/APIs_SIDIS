package com.example.controller;

import com.example.model.Product;
import com.example.repositories.ProductRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQReceiverProduct {
    @Autowired
    ProductRepository repository;
    private static Logger logger = LogManager.getLogger(RabbitMQReceiverProduct.class.toString());
    @RabbitListener(queues= "#{autoDeleteQueueProduct.name}")
    public void consumeJsonMessage(String pro)throws JsonProcessingException {
        logger.info("MenuOrder listener invoked - Consuming Message with MenuOrder Identifier : " + pro);

        ObjectMapper objectMapper = new ObjectMapper();
        Product obj=objectMapper.readValue(pro, Product.class);


        //jwtService.createJWT(obj);

        repository.save(obj);
    }
    /*@RabbitHandler
    public void receiver(Product product) {
        repository.save(product);
        logger.info("MenuOrder listener invoked - Consuming Message with MenuOrder Identifier : " + product.toString());
    }*/
}
