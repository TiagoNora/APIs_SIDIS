package com.example.service;

import com.example.model.Vote;
import com.example.model.VoteUpdate;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQSenderVote {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    //private AmqpTemplate amqpTemplate;

    public String exchange = "VoteUpdate";
    private static Logger logger = LogManager.getLogger(RabbitMQSenderVote.class.toString());
    public void send(Vote vote) throws JsonProcessingException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String Json=ow.writeValueAsString(vote);
        rabbitTemplate.convertAndSend(exchange,"",Json);
        //amqpTemplate.convertAndSend(exchange,"", vote);
        logger.info("Sending Message to the Queue : " + vote.toString());
    }
}
