package com.common.kafkacommonutils.error;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.MessageListenerContainer;

public class KafkaErrorHandler implements CommonErrorHandler {

    @Override
    public boolean handleOne(Exception ex,
                             ConsumerRecord<?, ?> record,
                             Consumer<?, ?> consumer,
                             MessageListenerContainer container) {

        System.err.println("Kafka consumer error");
        System.err.println("Topic      : " + record.topic());
        System.err.println("Partition  : " + record.partition());
        System.err.println("Offset     : " + record.offset());
        System.err.println("Key        : " + record.key());
        System.err.println("Value      : " + record.value());
        System.err.println("Exception  : " + ex.getMessage());

        /*
         * true  -> offset WILL be committed
         * false -> offset will NOT be committed
         */
        return true; // skip bad message
    }

    @Override
    public void handleOtherException(Exception ex,
                                     Consumer<?, ?> consumer,
                                     MessageListenerContainer container,
                                     boolean batchListener) {

        System.err.println("Kafka infrastructure error: " + ex.getMessage());
    }

    /**
     * We are NOT seeking, so no retries.
     */
    @Override
    public boolean seeksAfterHandling() {
        return false;
    }
}