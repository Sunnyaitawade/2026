package com.common.kafkacommonutils.consumer;
import com.fasterxml.jackson.databind.ObjectMapper;


public abstract class BaseKafkaListener<T> {


    private final ObjectMapper mapper = new ObjectMapper();
    private final Class<T> type;

    protected BaseKafkaListener(Class<T> type) {
        this.type = type;
    }

    protected T convert(String message) {
        try {
            return mapper.readValue(message, type);
        } catch (Exception e) {
            throw new RuntimeException("Kafka message conversion failed", e);
        }
    }
}