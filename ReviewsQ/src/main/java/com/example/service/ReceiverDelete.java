package com.example.service;

import com.example.model.Review;
import com.example.repository.ReviewRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

//@EnableRabbit
//@Component
//@RabbitListener(queues = "reviewsDelete1_queue_fanout", id = "listener2")
@Service
public class ReceiverDelete {
    @Autowired
    ReviewRepository repository;
    @Autowired
    ReviewService service;
    private static Logger logger = LogManager.getLogger(ReceiverDelete.class.toString());
    /*@RabbitHandler
    public void receiver(int id) {
        repository.deleteByIdReview(id);
        logger.info("MenuOrder listener invoked - Consuming Message with MenuOrder Identifier : " + id);
    }*/
    @RabbitListener(queues= "#{autoDeleteQueueDelete.name}")
    public void consumeJsonMessage(int rev)throws JsonProcessingException {
        logger.info("MenuOrder listener invoked - Consuming Message with MenuOrder Identifier : " + rev);

        //ObjectMapper objectMapper = new ObjectMapper();
        //Review obj=objectMapper.readValue(rev, Review.class);


        //jwtService.createJWT(obj);

       // repository.deleteByIdReview(rev);
        service.deleteById(rev);
    }
}
