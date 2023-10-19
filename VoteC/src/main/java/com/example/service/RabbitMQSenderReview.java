package com.example.service;
import com.example.model.Review;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQSenderReview {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    public String exchange = "RevVote";

    private static Logger logger = LogManager.getLogger(RabbitMQSender.class.toString());
    public void sendJsonMessage(Review review) throws JsonProcessingException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String Json=ow.writeValueAsString(review);
        rabbitTemplate.convertAndSend(exchange,"",Json);
        logger.info("Sending Message to the Queue : " + Json);
    }


}
