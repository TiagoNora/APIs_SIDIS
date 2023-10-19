package com.example.controller;

import com.example.model.Vote;
import com.example.repositories.VoteRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/*@EnableRabbit
@Component
@RabbitListener(queues = "votes1_queue_fanout", id = "listener")*/
@Service
public class RabbitMQReceiverVote {
    @Autowired
    VoteRepository repository;
    private static Logger logger = LogManager.getLogger(RabbitMQReceiverVote.class.toString());
    /*@RabbitHandler
    public void receiver(Vote vote) {
        repository.save(vote);
        logger.info("MenuOrder listener invoked - Consuming Message with MenuOrder Identifier : " + vote.toString());
    }*/
    @RabbitListener(queues= "#{autoDeleteQueueVote.name}")
    public void consumeJsonMessage(String str)throws JsonProcessingException {
        logger.info("MenuOrder listener invoked - Consuming Message with MenuOrder Identifier : " + str);

        ObjectMapper objectMapper = new ObjectMapper();
        Vote obj=objectMapper.readValue(str, Vote.class);


        //jwtService.createJWT(obj);

        repository.save(obj);
    }
}
