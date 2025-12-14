package com.common.kafkacommonutils.consumer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.retrytopic.RetryTopicHeaders;
import org.springframework.messaging.MessageHeaders;


public abstract class BaseKafkaListener<T> {
    protected final ObjectMapper objectMapper;
    private final Class<T> type;

    protected BaseKafkaListener(ObjectMapper objectMapper, Class<T> type) {
        this.objectMapper = objectMapper;
        this.type = type;
    }

    protected T convert(String message) {
        try {
            return objectMapper.readValue(message, type);
        } catch (Exception e) {
            throw new RuntimeException("Kafka message conversion failed", e);
        }
    }

    protected int getRetryAttempt(MessageHeaders headers) {
        return headers.containsKey(RetryTopicHeaders.DEFAULT_HEADER_ATTEMPTS)
                ? (int) headers.get(RetryTopicHeaders.DEFAULT_HEADER_ATTEMPTS)
                : 0;
    }
}