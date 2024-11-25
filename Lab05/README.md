# 112981

# Lab 4: Objective of this lab

- Develop web projects with Spring Boot.
- Create and persist entities into a relational database using Spring Data.
- Deploy Spring Boot application in Docker.

## Table of Contents


---

## Configure Apache Kafka with Docker

### Apache Kafka

Apache Kafka is an open-source distributed `event streaming` platform used by thousands of organizations to build:

- **High-performance data pipelines**
- **Streaming analytics applications**
- **Data integration solutions**
- **Mission-critical distributed systems**

Kafka allows users to **manage real-time data streams** efficiently. It supports high-throughput and fault-tolerant processing, making it ideal for applications where reliable and scalable message queuing is required.

#### Key Features
- **Topics**: Kafka organizes messages into categories called *topics*, enabling logical separation of data streams.
- **Producers & Consumers**: Applications can act as producers (sending messages to topics) or consumers (reading messages from topics).
- **Retention Policies**: Messages can be retained for a configurable duration, regardless of whether they have been consumed.


### Kafdrop: Kafka Management Tool

**Kafdrop** is a lightweight web UI for managing Kafka clusters. It provides a visual interface to:

- View and manage Kafka topics
- Inspect messages within topics
- Monitor consumer groups and their offsets
- Debug Kafka configurations and message flows

### Where to find the KafDrop UI, how to creare a topic and how to produce and consume messages

#### Step 1: Start the services

```bash
docker compose up -d
```

This will deploy Kafka (single-node) along with **Kafdrop**, accessible at [http://localhost:9009](http://localhost:9009).


#### Step 2: Create a Kafka Topic

Run the following command to create a new topic named, giving it a `<topic_name>`:
```bash
docker exec lab05-kafka-1 kafka-topics --create --topic <topic_name> --partitions 1 --replication-factor 1 --bootstrap-server kafka:9092
```

You can verify the topic creation in **Kafdrop** by navigating to [http://localhost:9009](http://localhost:9009) and inspecting the topic list.


#### Step 3: Consume Messages

To start consuming messages from the `<topic_name>` topic, run:
```bash
docker exec lab05-kafka-1 kafka-console-consumer --topic <topic_name> --from-beginning --bootstrap-server kafka:9092
```


#### Step 4: Publish Messages

To publish messages to the `lab05` topic:
1. Open a new terminal.
2. Enter the Kafka container:
```bash
docker exec -it lab05-kafka-1 bash
```
3. Run the producer command:
```bash
kafka-console-producer --topic lab05 --broker-list kafka:9092
```
4. Type any message and press **Enter**. The message will be sent to the topic.


### Answer to ex. 1_f

#### What happens if you open multiple consumer terminals?
- If I open multiple consumer terminals and connect them to the same topic:
  - Each consumer terminal will receive all the messages sent to the topic. This is because, by default, consumers are independent, so they all process the same stream of data. However, there is the concept of **consumer groups** that allows multiple consumers to work together within a group.

#### And if you open multiple terminals with producers? 
- If I open multiple producer terminals and send messages to the same topic, all messages from all producers will be appended to the topic's log. Kafka is designed to handle high-throughput and concurrent writes, so multiple producers writing to the same topic works seamlessly.

#### What happens with old messages? 
- **Retention Policy:** Kafka does not automatically delete messages after they are consumed. Instead, it retains them based on the topic's retention policy (`KAFKA_LOG_RETENTION_MS` in the *Docker Compose* file). In this setup, the retention is configured to keep messages for 10 seconds (`KAFKA_LOG_RETENTION_MS: 10000`).
- **Effect on Consumers:**
  - If a new consumer connects to the topic within the retention window (10 seconds), it can use the `--from-beginning` flag to read all messages, including old ones.
  - If the retention period has elapsed, old messages are deleted, and new consumers will not see those messages.

#### Conclusions
- Kafka allows multiple producers and consumers to interact with a topic efficiently.
- Messages are retained based on the configured retention period, not consumption status.


---

## Create a producer and consumer

This task demonstrates how to create a Kafka **producer** and **consumer** using Python, employing the `kafka-python` library within a Poetry project. Here's a step-by-step explanation:

### Setting Up the Project with Poetry

#### Poetry
[Poetry](https://python-poetry.org/) is a Python dependency management tool that simplifies the creation and management of Python projects.

#### Steps to Create the Project

1. **Install Poetry** (if not already installed):
```bash
pipx install poetry
```

2. **Create a New Project:**
```bash
poetry new kafka_project
cd kafka_project
```

3. **Add Dependencies:**
Add the `kafka-python` library to the project:
```bash
poetry add kafka-python
```

4. **Activate the Virtual Environment (or use `poetry shell`):**
```bash
python3 -m venv venv
source venv/bin/activate
```

5. **Create Files for Producer and Consumer:**
Add `producer_example.py` and `consumer_example.py` to the project's `kafka_project` directory.

### **Python Version Issue and the Workaround**

#### **Problem**
`kafka-python` depends on an internal library called `six.moves`. With Python versions >= 3.12, some parts of this dependency are no longer supported, leading to errors.

#### **Workaround in the Code**
To bypass the issue, the code **mocks** the missing `kafka.vendor.six.moves` module:

```python
import sys
import types

# Mock the 'kafka.vendor.six.moves' module to bypass missing dependency issues
m = types.ModuleType('kafka.vendor.six.moves', 'Mock module')
setattr(m, 'range', range)  # Mocking the range function
sys.modules['kafka.vendor.six.moves'] = m
```

This ensures that `kafka-python` functions properly without modifying its source code.

### **Kafka Producer**

#### **Imports and Configuration**
```python
from kafka import KafkaProducer
import json
```

- **`KafkaProducer`**: Used to produce messages to Kafka.
- **`json`**: Helps serialize Python dictionaries to JSON strings.

#### **Kafka Settings**
```python
nMec = 112981
bootstrap_servers = 'localhost:29092'
topic_name = f'lab05_{nMec}'
```

- **`bootstrap_servers`**: Kafka broker's address.

#### **Producer Initialization**
```python
producer = KafkaProducer(
    bootstrap_servers=bootstrap_servers,
    value_serializer=lambda value: json.dumps(value).encode('utf-8')
)
```
- **`value_serializer`**: Converts Python dictionaries to JSON and encodes them into bytes.

#### **Fibonacci Message Generation and Sending**
```python
a, b = 0, 1
generated_number = 1

while generated_number <= nMec:
    message = {
        'nMec': str(nMec),
        'generatedNumber': generated_number,
        'type': 'fibonacci'
    }
    producer.send(topic_name, value=message)
    print(f"Sent message: {message}")
    a, b = b, a + b  # Generate the next Fibonacci number
    generated_number = b
```

- Generates Fibonacci numbers.
- Constructs a message dictionary with the format:
  ```python
  {'nMec': '99999', 'generatedNumber': 1, 'type': 'fibonacci'}
  ```
- Sends the message to the Kafka topic using `producer.send`.

#### **Closing the Producer**
```python
producer.close()
```

### **Kafka Consumer**

#### **Imports and Configuration**
```python
from kafka import KafkaConsumer, TopicPartition
```

- **`KafkaConsumer`**: Used to consume messages from Kafka.
- **`TopicPartition`**: Allows handling specific partitions of a topic.

#### **Consumer Initialization**
```python
consumer = KafkaConsumer(
    topic_name,
    bootstrap_servers=bootstrap_servers,
    group_id='consumers_1',
    enable_auto_commit=False,
    auto_offset_reset='earliest'
)
```

- **`group_id`**: Identifies the consumer group for managing offsets.
- **`enable_auto_commit`**: Disables automatic offset commits (manual control over message acknowledgment) **-> I changed this for testing purposes.**
- **`auto_offset_reset='earliest'`**: Ensures messages are read from the beginning if no previous offset is found.

#### **Partition Handling**
```python
PARTITIONS = []
for partition in consumer.partitions_for_topic(topic_name):
    PARTITIONS.append(TopicPartition(topic_name, partition))
end_offsets = consumer.end_offsets(PARTITIONS)
print(end_offsets)
```

- Retrieves the end offsets for each partition in the topic.

#### **Consuming Messages**
```python
try:
    for message in consumer:
        print(f"Received message: {message.value.decode('utf-8')}")
except KeyboardInterrupt:
    print("\nConsumer interrupted by user. Closing...")
finally:
    consumer.close()
    print("Consumer closed.")
```

- Processes each message from the topic and decodes it from bytes to a string.

### **Running the Applications**

1. **Start the Kafka Environment:**
    Ensure **`Kafka`** and **`Zookeeper`** are running using `Docker Compose`.

2. **Run the Producer:**
```bash
poetry run python producer_example.py
```
   This will produce Fibonacci sequence messages to the topic.

3. **Run the Consumer:**
```bash
poetry run python consumer_example.py
```
   The consumer will read and print messages from the topic.


### Answers to ex. 2_c and 2_d

#### **What is the last message?**

The last message produced by the Kafka topic "lab05_112981" is:

```json
{"nMec": "112981", "generatedNumber": 75025, "type": "fibonacci"}
```

This corresponds to the largest Fibonacci number less than or equal to `112981`, which matches the `nMec` value.
Also, corresponds to the last message produced for the sequence and, if within the retention time, the last message consumed.

#### **If you run the consumer multiple times, does it read all the messages? Why?**

No, the consumer does **not necessarily read all messages again** if I run it multiple times. 
The reason lies in how **consumer commit** and **retention time** work in Kafka:

1. **Consumer Commit**:
   - Kafka tracks the offset (position) for each **consumer group** in a topic. If the consumer has the `enable_auto_commit` option enabled (default), it will periodically save its progress (offset) back to Kafka.
   - On subsequent runs, the consumer starts from the last committed offset, skipping already-read messages.

2. **Retention Time**:
   - The topic's messages are retained for a limited time (`KAFKA_LOG_RETENTION_MS: 10000`, i.e., 10 seconds). If messages expire, they will no longer be available for the consumer to read. 
   - And if I go to the Kafka Broker and try to see the messages on the topic, I can only see the messages published in the last <`retention_time`> seconds.

So, if:
- the consumer commits offsets, it will only consume unread messages on subsequent runs;
- messages are removed from the topic after the retention period, they are no longer available to any consumer.

---

## Create a consumer (& producer) in Java integrated with Spring Boot

This exercise demonstrates how to integrate Apache Kafka with a Spring Boot application to consume and produce messages, leveraging Kafka's distributed nature to interact with Python-based (or other language-based) producers and consumers.
For the exercise, important information was obtained from the [Spring Kafka Quick Tour](https://docs.spring.io/spring-kafka/reference/quick-tour.html), mainly regarding the [KafkaTemplate](https://docs.spring.io/spring-kafka/reference/kafka/sending-messages.html).

### Setting Up the Spring Boot Project

#### **Project Creation**
1. **Generate a Spring Boot project**:
   Use [Spring Initializr](https://start.spring.io/) to create a project with the following dependency:
   - Spring for Apache Kafka

2. **Configure the project structure**:
   Organize the project into packages:
   - `models` (to define message models for deserialization)
   - `services` (for Kafka producer and consumer logic)
   - `main application` for bootstrapping.

### Configuration: `application.properties`

The Kafka-related configurations were added to `application.properties` to enable smooth integration:

```properties
spring.application.name=lab5_3

# Kafka broker settings
spring.kafka.bootstrap-servers=localhost:29092

# Producer configuration
spring.kafka.producer.key-serializer=org.apache.kafka.common.serialization.StringSerializer
spring.kafka.producer.value-serializer=org.springframework.kafka.support.serializer.JsonSerializer
spring.kafka.producer.properties.spring.json.add.type.headers=false

# Consumer configuration
spring.kafka.consumer.group-id=consumers_1
spring.kafka.consumer.auto-offset-reset=earliest
spring.kafka.consumer.key-deserializer=org.apache.kafka.common.serialization.StringDeserializer
spring.kafka.consumer.value-deserializer=org.springframework.kafka.support.serializer.JsonDeserializer
spring.kafka.consumer.properties.spring.json.trusted.packages=*
spring.kafka.consumer.properties[spring.json.value.default.type]=ex03.lab5.ies.lab5_3.models.Message
```

Key points:
- **Producer and Consumer settings**: Configure serializers for converting Java objects to JSON and vice versa.
- **Trusted packages**: Allows deserialization of custom objects from JSON messages.

### Message Model: `Message` Class

This part of the exercise was made with the help of:
  - [How to implement a Serializer in Spring Boot to deserialize the message produced](https://howtodoinjava.com/kafka/spring-boot-jsonserializer-example/)

This class encapsulates the message structure to **`match the format sent by the Python producer`**:

```java
package ex03.lab5.ies.lab5_3.models;

public class Message {
    private String nMec;
    private int generatedNumber;
    private String type;

    // Constructors, Getters, Setters, and toString method
}
```

- **Purpose**: Represents the message data (`nMec`, `generatedNumber`, `type`) **`for deserialization and internal processing`**.
- **Compatibility**: Aligns with the Python producer’s message format:
  ```python
  {'nMec': '112981', 'generatedNumber': 1, 'type': 'fibonacci'}
  ```

### Kafka Consumer

The consumer listens to messages from the topic **and deserializes them** into **`Message` objects**.

#### **Implementation: `KafkaConsumerService`**

```java
package ex03.lab5.ies.lab5_3.services;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ex03.lab5.ies.lab5_3.models.Message;

@Service
public class KafkaConsumerService {

    @KafkaListener(topics = "lab05_112981", groupId = "consumers_1")
    public void listen(Message message) {
        System.out.println("Received message: " + message.toString());
    }
}
```

- **Annotations**:
  - `@KafkaListener`: Configures the service to consume messages from the topic `lab05_112981`.
  - `groupId`: Ensures consumer group tracking for Kafka's offset management.

- **Logic**:
  - Converts JSON messages from the topic into `Message` objects using the configured deserializer (see `application.properties` configuration).
  - Prints the received message.

### Kafka Producer

The producer generates Fibonacci sequence messages and sends them to the same topic (this only runs once when running the app).

#### **Implementation: `KafkaProducerService`**

```java
package ex03.lab5.ies.lab5_3.services;

import ex03.lab5.ies.lab5_3.models.Message;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class KafkaProducerService {

    private KafkaTemplate<String, Object> template;
    private int nMec = 112981;

    public KafkaProducerService(KafkaTemplate<String, Object> kafkaTemplate) {
        this.template = kafkaTemplate;
    }

    public void produceMessages() {
        int a = 0, b = 1;

        while (a <= nMec) {
            Message message = new Message(nMec, a, "fibonacci");
            sendToKafka(message);

            int next = a + b;
            a = b;
            b = next;
        }
    }

    private void sendToKafka(final Message data) {
        ProducerRecord<String, Object> record = new ProducerRecord<>("lab05_" + nMec, data);
        CompletableFuture<SendResult<String, Object>> future = template.send(record);

        future.whenComplete((result, ex) -> {
            if (ex == null) {
                System.out.println("Message sent successfully: " + data);
            } else {
                System.err.println("Error sending message: " + data + ", due to: " + ex.getMessage());
            }
        });
    }
}
```

- **Fibonacci Generation**: The sequence is generated up to `nMec`, similar to the Python producer.
- **Sending Messages**:
  - Messages are serialized as JSON using Spring’s `KafkaTemplate`.
  - A `CompletableFuture` is used for asynchronous feedback on message delivery.

### **Main Application**

The Spring Boot application runs both the producer and consumer:

```java
package ex03.lab5.ies.lab5_3;

import ex03.lab5.ies.lab5_3.services.KafkaProducerService;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Lab53Application {

    public static void main(String[] args) {
        SpringApplication.run(Lab53Application.class, args);
    }

    @Bean
    public ApplicationRunner runner(KafkaProducerService producer) {
        return args -> producer.produceMessages();
    }
}
```

- Automatically runs the `KafkaProducerService` to produce messages when the application starts.
- The **consumer** runs **`as a background service`**.

### **Python Integration**

#### **Testing with Python Producer**
Run the Python `producer.py` script to produce messages:
```bash
poetry run python producer_example.py
```

Verify that messages appear in the Spring Boot application logs, confirming the consumer is working.

#### **Testing with Python Consumer**
Run the Python `consumer.py` script to read messages:
```bash
poetry run python consumer_example.py
```

Send new messages using the Spring Boot producer and verify that Python consumes them.

### **Key Takeaways**
- **Multi-language integration**: Kafka’s distributed architecture allows seamless communication between Python and Java.
- **Serialization/Deserialization**: Proper JSON serializers and deserializers are crucial for cross-language message handling.
- **Spring Kafka** simplifies consumer/producer logic while providing robust configurations for scalability and reliability.






[Kafka Documentation](https://kafka.apache.org/documentation/)
[Poetry](https://python-poetry.org/)

[How to implement a Serializer in Spring Boot to deserialize the message produced](https://howtodoinjava.com/kafka/spring-boot-jsonserializer-example/)
[KafkaTemplate](https://docs.spring.io/spring-kafka/reference/kafka/sending-messages.html)
