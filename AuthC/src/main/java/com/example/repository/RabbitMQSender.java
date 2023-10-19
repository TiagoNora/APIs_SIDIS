package com.example.repository;

//import ch.qos.logback.core.net.ObjectWriter;
import com.example.model.JWT;

import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class RabbitMQSender {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private FanoutExchange fanoutExchange;
    private static Logger logger = LogManager.getLogger(RabbitMQSender.class.toString());
    public void sendJsonMessage(JWT jwt) throws JsonProcessingException{
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String Json=ow.writeValueAsString(jwt);
        rabbitTemplate.convertAndSend(fanoutExchange.getName(),"",Json);
        logger.info("Sending Message to the Queue : " + Json);
    }

    /*@Autowired
    private AmqpTemplate amqpTemplate;

    public String exchange = "authentications_exchange";
    private static Logger logger = LogManager.getLogger(RabbitMQSender.class.toString());
    public void send(JWT jwt) {
        amqpTemplate.convertAndSend(exchange,"", jwt);
        logger.info("Sending Message to the Queue : " + jwt.toString());
    }*/
}
