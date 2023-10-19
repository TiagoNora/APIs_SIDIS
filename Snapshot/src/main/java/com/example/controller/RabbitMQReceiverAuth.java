package com.example.controller;
import com.example.model.JWT;
import com.example.repositories.JWTRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class RabbitMQReceiverAuth {
    private static Logger logger = LogManager.getLogger(RabbitMQReceiverAuth.class.toString());
    @Autowired
    JWTRepository repository;
    @RabbitListener(queues= "#{autoDeleteQueueAuth.name}")
    public void consumeJsonMessage(String jwt)throws JsonProcessingException{
        logger.info("MenuOrder listener invoked - Consuming Message with MenuOrder Identifier : " + jwt);

        ObjectMapper objectMapper = new ObjectMapper();
        JWT obj=objectMapper.readValue(jwt, JWT.class);


        //jwtService.createJWT(obj);

        repository.save(obj);
    }

   /* @Autowired
    JWTRepository repository;
    private static Logger logger = LogManager.getLogger(RabbitMQReceiver.class.toString());
    @RabbitHandler
    public void receiver(JWT jwt) {
        repository.save(jwt);
        logger.info("MenuOrder listener invoked - Consuming Message with MenuOrder Identifier : " + jwt.toString());
    }*/
}
