package com.common.kafkacommonutils.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;


@Component
public class KafkaEventPublisher {


    private final KafkaTemplate<String, Object> kafkaTemplate;


    public KafkaEventPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }


    public <T> void publish(String topic, T event) {
        kafkaTemplate.send(topic, event);
    }
}