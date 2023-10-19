package com.example.repositories;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {
/*
    @Value("${queue.name}")
    private String queueName;

    @Value("${xchange.name}")
    private String directXchangeName;

    @Bean
    public Queue queue() {
        return new Queue(queueName);
    }

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(directXchangeName);
    }

    @Bean
    public Binding binding(DirectExchange exchange, Queue queue) {
        return BindingBuilder.bind(queue).to(exchange).with("roytuts");
    }
 */
    @Bean
    public DirectExchange fanout () {
        return new DirectExchange("jwt");
    }
    @Bean
    public Queue jwtQueue() {
        return new AnonymousQueue();
    }
    @Bean
    public Binding binding (DirectExchange fanout, Queue jwtQueue){
        return BindingBuilder.bind(jwtQueue).to(fanout).with("jwt");
    }


    @Bean
    public DirectExchange fanout1 () {
        return new DirectExchange("product");
    }
    @Bean
    public Queue productQueue() {
        return new AnonymousQueue();
    }
    @Bean
    public Binding binding1 (DirectExchange fanout1, Queue productQueue){
        return BindingBuilder.bind(productQueue).to(fanout1).with("product");
    }

    @Bean
    public DirectExchange fanout2 () {
        return new DirectExchange("review");
    }
    @Bean
    public Queue reviewQueue() {
        return new AnonymousQueue();
    }
    @Bean
    public Binding binding2 (DirectExchange fanout2, Queue reviewQueue){
        return BindingBuilder.bind(reviewQueue).to(fanout2).with("review");
    }

    @Bean
    public DirectExchange fanout3 () {
        return new DirectExchange("vote");
    }
    @Bean
    public Queue voteQueue() {
        return new AnonymousQueue();
    }
    @Bean
    public Binding binding3 (DirectExchange fanout3, Queue voteQueue){
        return BindingBuilder.bind(voteQueue).to(fanout3).with("vote");
    }

    @Bean
    public ServerJWT server() {
        return new ServerJWT();
    }
    @Bean
    public ServerProduct server1() {
        return new ServerProduct();
    }
    @Bean
    public ServerReview server2() {
        return new ServerReview();
    }
    @Bean
    public ServerVote server3() {
        return new ServerVote();
    }


}
