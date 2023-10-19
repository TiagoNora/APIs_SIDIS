package com.example.controller;

import com.example.model.Review;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ClientReview {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private DirectExchange exchange2;
    public List<Review> send() {
        int n= 0;
        List<Review> response = (List<Review>) rabbitTemplate.convertSendAndReceive(exchange2.getName(),"review", n);

        System.out.println("Got " + response + "");
        return response;
    }

}
