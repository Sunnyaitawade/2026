package com.common.kafkacommonutils.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serializer;


public class GenericJsonSerializer<T> implements Serializer<T> {
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public byte[] serialize(String topic, T data) {
        try {
            return mapper.writeValueAsBytes(data);
        } catch (Exception e) {
            throw new RuntimeException("Kafka serialization failed", e);
        }
    }
}