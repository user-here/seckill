package org.example.seckill.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    public static final String QUEUE1 = "queue1";
    public static final String QUEUE2 = "queue2";
    public static final String ROUTING_KEY_RED = "queue.red";
    public static final String ROUTING_KEY_BLUE = "queue.blue";
    public static final String EXCHANGE = "exchange";


    @Bean
    public Queue queue1() {
        return new Queue(QUEUE1);
    }

    @Bean
    public Queue queue2() {
        return new Queue(QUEUE2);
    }

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(EXCHANGE);
    }

    @Bean
    public Binding queueRed() {
        return BindingBuilder.bind(queue1()).to(exchange()).with(ROUTING_KEY_RED);
    }

    @Bean
    public Binding queueBlue() {
        return BindingBuilder.bind(queue2()).to(exchange()).with(ROUTING_KEY_BLUE);
    }

}
