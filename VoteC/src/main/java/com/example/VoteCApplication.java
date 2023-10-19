package com.example;

import com.example.controller.ClientAuth;
import com.example.controller.ClientRev;
import com.example.controller.ClientVote;
import com.example.model.JWT;
import com.example.model.Review;
import com.example.model.Vote;
import com.example.repository.JWTRepository;
import com.example.repository.ReviewRepository;
import com.example.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class VoteCApplication {
    public static void main(String[] args) {

        SpringApplication.run(VoteCApplication.class, args);
    }
    @Autowired
    ClientAuth clientAuth;
    @Autowired
    ClientRev clientRev;

    @Autowired
    ClientVote clientVote;

    @Bean
    public CommandLineRunner loadData(JWTRepository repository, ReviewRepository reviewRepository, VoteRepository voteRepository) {
        return (args) -> {
            // save a couple of customers
            if(repository.count() == 0){
                List<JWT> lista = clientAuth.send();
                if (lista != null){
                    repository.saveAll(lista);
                }
            }
            if(reviewRepository.count() == 0){
                List<Review> lista = clientRev.send();
                if (lista != null){
                    reviewRepository.saveAll(lista);
                }
            }

            if(voteRepository.count() == 0){
                List<Vote> lista = clientVote.send();
                if (lista != null){
                    voteRepository.saveAll(lista);
                }
            }
            //repository.save(new Customer("Jack", "Bauer"));
        };

    }

}
