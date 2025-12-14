# Kafka Common Utils

## Overview

Kafka Common Utils is a lightweight utility library designed to standardize **Kafka consumer patterns** across Spring Boot microservices. It focuses on **safe offset handling**, **retry visibility**, and **DLT observability**, making it especially suitable for **payments and event-driven systems**.

This library provides:

* Generic base Kafka listener abstraction
* Consistent JSON deserialization
* Header-based retry attempt tracking
* First-class support for Spring Kafka `@RetryableTopic`

---

## ✨ New Features (Latest Update)

### 1. Generic `BaseKafkaListener<T>`

A reusable abstract listener that:

* Centralizes JSON → Object conversion
* Avoids duplicate deserialization logic
* Promotes consistent error handling

```java
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
}
```

---

### 2. Header-Based Retry Attempt Tracking ✅

The library now supports **retry attempt visibility using Kafka headers** when used with Spring Kafka `@RetryableTopic`.

Spring Kafka automatically injects retry metadata into message headers, which can be consumed for:

* Logging
* Metrics
* Conditional retry logic
* Debugging production failures

#### Supported Headers

| Header                          | Description                  |
| ------------------------------- | ---------------------------- |
| `retry_topic-attempts`          | Current retry attempt number |
| `retry_topic-original-topic`    | Original topic name          |
| `retry_topic-exception-message` | Exception message            |
| `retry_topic-exception-class`   | Exception class              |
| `kafka_receivedTopic`           | Current topic                |
| `kafka_offset`                  | Record offset                |

---

### 3. Retry-Aware Consumer Example

```java
@RetryableTopic(attempts = "3", dltTopicSuffix = "-DLT")
@KafkaListener(topics = KafkaTopics.PAYMENT_EVENT, groupId = "libtest-group")
public void consume(String message, @Headers MessageHeaders headers) {

    PaymentEvent event = convert(message);

    int attempt = (int) headers.getOrDefault(
            RetryTopicHeaders.DEFAULT_HEADER_ATTEMPTS, 0
    );

    String topic = (String) headers.get(KafkaHeaders.RECEIVED_TOPIC);

    log.info("Topic={} | RetryAttempt={} | Event={}", topic, attempt, event);

    if (event.isRetryableFailure()) {
        throw new RuntimeException("Simulated failure");
    }
}
```

---

### 4. DLT Handling with Full Context

```java
@DltHandler
public void dlt(String message, @Headers MessageHeaders headers) {

    PaymentEvent event = convert(message);

    String originalTopic = (String)
            headers.get(RetryTopicHeaders.DEFAULT_HEADER_ORIGINAL_TOPIC);

    int attempts = (int)
            headers.get(RetryTopicHeaders.DEFAULT_HEADER_ATTEMPTS);

    String error = (String)
            headers.get(RetryTopicHeaders.DEFAULT_HEADER_EXCEPTION_MESSAGE);

    log.error("DLT | OriginalTopic={} | Attempts={} | Error={} | Event={}",
            originalTopic, attempts, error, event);
}
```

---

## 🔄 Offset Management Behavior

| Scenario              | Offset Commit         |
| --------------------- | --------------------- |
| Successful processing | ✅ Committed           |
| Retry attempt         | ❌ Not committed       |
| Sent to DLT           | ✅ Committed after DLT |

This ensures **at-least-once processing** with full retry transparency.

---

## 📦 Use Cases

* Payment event processing
* Fraud / non-fraud workflows
* Audit-safe event consumers
* Shared Kafka consumer standards across teams

---

## 🚀 Roadmap

* Retry header utility helper (`RetryHeaderUtil`)
* Centralized DLT consumer module
* Micrometer metrics integration
* Support for non-retryable exception classification

---

## 🛠 Requirements

* Java 17+
* Spring Boot 3.x
* Spring Kafka
* Apache Kafka (ZooKeeper or KRaft)

---

## 📄 License

Internal / Company Use
