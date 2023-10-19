package com.example.services;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    @Bean
    public FanoutExchange fanout () {
        return new FanoutExchange("Products");
    }
    @Bean
    public Queue autoDeleteQueue() {
        return new AnonymousQueue();
    }
    @Bean
    public Binding binding (FanoutExchange fanout, Queue autoDeleteQUeue){
        return BindingBuilder.bind(autoDeleteQUeue).to(fanout);
    }

    @Bean
    public DirectExchange directExchange () {
        return new DirectExchange("product");
    }

}





