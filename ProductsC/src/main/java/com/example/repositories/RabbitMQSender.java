package com.example.repositories;


import com.example.model.Product;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class RabbitMQSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private FanoutExchange fanoutExchange;
    private static Logger logger = LogManager.getLogger(RabbitMQSender.class.toString());
    public void sendJsonMessage(Product product) throws JsonProcessingException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String Json=ow.writeValueAsString(product);
        rabbitTemplate.convertAndSend(fanoutExchange.getName(),"",Json);
        logger.info("Sending Message to the Queue : " + Json);
    }
    /*@Autowired
    private AmqpTemplate amqpTemplate;

    public String exchange = "products_exchange";
    private static Logger logger = LogManager.getLogger(RabbitMQSender.class.toString());
    public void send(Product product) {
        amqpTemplate.convertAndSend(exchange,"", product);
        logger.info("Sending Message to the Queue : " + product.toString());
    }*/
}
