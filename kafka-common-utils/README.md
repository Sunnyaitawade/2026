# Kafka Common Utils

A reusable **Kafka utility library for Spring Boot microservices** that centralizes Kafka producer, consumer, serialization, and configuration logic.
Designed to reduce boilerplate and ensure consistent Kafka usage across services.

---

## 🚀 Features

* Generic Kafka **Producer** & **Consumer**
* JSON serialization / deserialization
* Centralized Kafka configuration
* Reusable DTOs & topic constants
* Easy plug-and-play integration
* Java 17 + Spring Boot compatible
* Production-ready structure (payments / fintech friendly)

---

## 📦 Tech Stack

* Java 17
* Spring Boot
* Spring Kafka
* Apache Kafka
* Jackson

---

## 📁 Module Structure

```
kafka-common-utils
│
├── config
│   ├── KafkaProducerConfig.java
│   ├── KafkaConsumerConfig.java
│
├── publisher
│   └── KafkaEventPublisher.java
│
├── consumer
│   └── BaseKafkaListener.java
│
├── serializer
│   ├── GenericJsonSerializer.java
│   └── GenericJsonDeserializer.java
│
├── model
│   └── PaymentEvent.java
│
├── constants
│   └── KafkaTopics.java
```

---

## 🔧 Installation

### 1️⃣ Build & Install Library

```bash
mvn clean install
```

This installs the library into your local Maven repository.

---

### 2️⃣ Add Dependency in Microservice

```xml
<dependency>
  <groupId>com.common</groupId>
  <artifactId>kafka-common-utils</artifactId>
  <version>1.0.1</version>
</dependency>
```

---

## ⚙️ Configuration

### application.properties

```properties
# Kafka bootstrap server
kafka.bootstrap-servers=localhost:9092

# Consumer
spring.kafka.consumer.group-id=payment-group
spring.kafka.consumer.auto-offset-reset=earliest
```

---

## ✉️ Publishing Events

```java
@RestController
@RequestMapping("/test")
public class TestProducerController {

    private final KafkaEventPublisher publisher;

    public TestProducerController(KafkaEventPublisher publisher) {
        this.publisher = publisher;
    }

    @PostMapping("/publish")
    public String publish(@RequestBody PaymentEvent event) {
        publisher.publish(KafkaTopics.PAYMENT_EVENT, event);
        return "Event published successfully";
    }
}
```

---

## 📥 Consuming Events

```java
@Component
public class PaymentEventListener extends BaseKafkaListener<PaymentEvent> {

    @Override
    @KafkaListener(
        topics = KafkaTopics.PAYMENT_EVENT,
        groupId = "payment-group"
    )
    public void listen(PaymentEvent event) {
        log.info("Received Payment Event: {}", event);
    }
}
```

---

## 🧪 Testing

### Postman API

```
POST http://localhost:8085/test/publish
```

```json
{
  "transactionId": "TXN-001",
  "amount": 1200.50,
  "status": "SUCCESS"
}
```

---

## 🧠 Topic Management

* Auto topic creation is enabled for local development
* For production, topics should be created explicitly with:

    * Partition count
    * Replication factor
    * Retention policy

Example:

```java
@Bean
public NewTopic paymentEventsTopic() {
    return TopicBuilder.name("payment-events")
        .partitions(3)
        .replicas(1)
        .build();
}
```

---

## 🏭 Production Best Practices

* Disable auto topic creation in production
* Use versioned topics (`payment-events.v1`)
* Add retry & Dead Letter Topics (DLT)
* Use headers for correlationId / traceId
* Consider Avro + Schema Registry
* Enable exactly-once semantics if required

---

## 🔐 Compatibility

| Component   | Version   |
| ----------- | --------- |
| Java        | 17+       |
| Spring Boot | 3.x / 4.x |
| Kafka       | 3.x+      |

---

## 📌 Roadmap

* Retry & Dead Letter Topic support
* Spring Boot auto-configuration starter
* Kafka headers support
* Avro & Schema Registry integration
* Reactive Kafka support

---

## 👨‍💻 Author

**Sunny Aitawade**
Senior Java Developer | Kafka | Spring Boot | Payments & FinTech

---

## 📄 License

This project is licensed for internal / educational use.
Open-source licensing can be added as needed.

---
 