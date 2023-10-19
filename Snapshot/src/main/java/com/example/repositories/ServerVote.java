package com.example.repositories;
import com.example.model.ProductDetailsDTO;
import com.example.model.Vote;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ServerVote {
    @Autowired
    VoteRepository repository;

    @RabbitListener(queues = "#{voteQueue.name}")
    public List<Vote> findProduct(int n) {
        return repository.findAll();
    }


}
