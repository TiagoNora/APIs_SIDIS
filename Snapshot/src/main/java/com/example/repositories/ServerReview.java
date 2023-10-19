package com.example.repositories;
import com.example.model.ProductDetailsDTO;
import com.example.model.Review;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ServerReview {
    @Autowired
    ReviewRepository repository;

    @RabbitListener(queues = "#{reviewQueue.name}")
    public List<Review> findProduct(int n) {
        return repository.findAll();
    }


}
