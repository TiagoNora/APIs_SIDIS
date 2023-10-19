package com.example;

import com.example.controller.ClientReview;
import com.example.model.Review;
import com.example.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class ReviewsQApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReviewsQApplication.class, args);
    }
    @Autowired
    ClientReview clientReview;

    @Bean
    public CommandLineRunner loadData(ReviewRepository reviewRepository) {
        return (args) -> {
            // save a couple of customers
            if(reviewRepository.count() == 0){
                List<Review> lista = clientReview.send();
                if (lista != null){
                    reviewRepository.saveAll(lista);
                }
            }
            //repository.save(new Customer("Jack", "Bauer"));
        };

    }

}
