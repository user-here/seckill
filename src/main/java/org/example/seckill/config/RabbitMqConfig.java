package org.example.seckill.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    public static final String QUEUE = "queue";
    public static final String EXCHANGE = "exchange";


    @Bean
    public Queue queue() {
        return new Queue("queue", true);
    }

}
