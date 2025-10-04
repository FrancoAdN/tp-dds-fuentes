package ar.edu.utn.dds.k3003.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
@ConditionalOnProperty(prefix = "amqp", name = "enabled", havingValue = "true")
public class AmqpConfig {

    @Value("${amqp.uri:}")
    private String amqpUri;

    @Value("${amqp.exchange:hechos.exchange}")
    private String exchangeName;

    @Value("${amqp.exchange.type:topic}")
    private String exchangeType;

    @Value("${amqp.queue:hechos.queue}")
    private String queueName;

    @Value("${amqp.routing-key:hecho.created}")
    private String routingKey;

    @Bean
    public ConnectionFactory rabbitConnectionFactory() {
        CachingConnectionFactory factory = new CachingConnectionFactory();
        if (amqpUri != null && !amqpUri.isBlank()) {
            factory.setUri(amqpUri);
        }
        return factory;
    }

    @Bean
    public AmqpAdmin amqpAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public Declarables topology() {
        Exchange exchange = switch (exchangeType.toLowerCase()) {
            case "direct" -> ExchangeBuilder.directExchange(exchangeName).durable(true).build();
            case "fanout" -> ExchangeBuilder.fanoutExchange(exchangeName).durable(true).build();
            case "headers" -> ExchangeBuilder.headersExchange(exchangeName).durable(true).build();
            default -> ExchangeBuilder.topicExchange(exchangeName).durable(true).build();
        };
        Queue queue = QueueBuilder.durable(queueName).build();
        Binding binding = BindingBuilder.bind(queue).to((Exchange) exchange).with(routingKey).noargs();
        return new Declarables(exchange, queue, binding);
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, Jackson2JsonMessageConverter converter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(converter);
        return template;
    }

    @Bean(name = "hechosListenerFactory")
    public SimpleRabbitListenerContainerFactory hechosListenerFactory(
            ConnectionFactory connectionFactory
    ) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        // No establecer Jackson converter aquí; usaremos el payload crudo/String y parsearemos manualmente
        factory.setAcknowledgeMode(AcknowledgeMode.AUTO);
        factory.setDefaultRequeueRejected(false); // no requeue on exceptions
        factory.setPrefetchCount(1);
        return factory;
    }
}


