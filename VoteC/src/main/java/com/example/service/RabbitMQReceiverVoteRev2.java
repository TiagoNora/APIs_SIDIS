package com.example.service;

import com.example.model.Vote;
import com.example.repository.VoteRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/*@EnableRabbit
@Component
@RabbitListener(queues = "votes1_queue_fanout", id = "listener")*/
@Service
public class RabbitMQReceiverVoteRev2 {
    @Autowired
    VoteRepository repository;

    @Autowired
    VoteService service;
    private static Logger logger = LogManager.getLogger(RabbitMQReceiverVoteRev2.class.toString());
    /*@RabbitHandler
    public void receiver(Vote vote) {
        repository.save(vote);
        logger.info("MenuOrder listener invoked - Consuming Message with MenuOrder Identifier : " + vote.toString());
    }*/
    @RabbitListener(queues= "#{autoDeleteQueueVoteRev2.name}")
    public void consumeJsonMessage(String str) throws IOException {
        logger.info("vote rev MenuOrder listener invoked - Consuming Message with MenuOrder Identifier : " + str);

        ObjectMapper objectMapper = new ObjectMapper();
        Vote obj=objectMapper.readValue(str, Vote.class);


        //jwtService.createJWT(obj);

        //repository.save(obj);

        service.createVote(obj);
    }
}
