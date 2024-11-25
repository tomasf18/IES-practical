# 112981

# Lab 4: Objective of this lab

 - Deploy Apache Kafka in Docker.  
 - Create an application to produce and consume messages with Apache Kafka and Spring Boot.

## Table of Contents

0. [How to run the project](#how-to-run-the-project)
1. [Configure Apache Kafka with Docker](#configure-apache-kafka-with-docker)
    - [Apache Kafka](#apache-kafka)
    - [Kafdrop: Kafka Management Tool](#kafdrop-kafka-management-tool)
    - [Where to find the KafDrop UI, how to creare a topic and how to produce and consume messages](#where-to-find-the-kafdrop-ui-how-to-creare-a-topic-and-how-to-produce-and-consume-messages)
    - [Answer to ex. 1_f](#answer-to-ex-1_f)
2. [Create a producer and consumer](#create-a-producer-and-consumer)
    - [Setting Up the Project with Poetry](#setting-up-the-project-with-poetry)
    - [Python Version Issue and the Workaround](#python-version-issue-and-the-workaround)
    - [Kafka Producer](#kafka-producer)
    - [Kafka Consumer](#kafka-consumer)
    - [Running the Applications](#running-the-applications)
    - [Answers to ex. 2_c and 2_d](#answers-to-ex-2_c-and-2_d)
3. [Create a consumer (& producer) in Java integrated with Spring Boot](#create-a-consumer--producer-in-java-integrated-with-spring-boot)
    - [Setting Up the Spring Boot Project](#setting-up-the-spring-boot-project)
    - [Configuration: `application.properties`](#configuration-applicationproperties)
    - [Message Model: `Message` Class](#message-model-message-class)
    - [Kafka Consumer](#kafka-consumer-1)
    - [Kafka Producer](#kafka-producer-1)
    - [Main Application](#main-application)
    - [Key Takeaways](#key-takeaways)
4. [Wrapping-up and integrating concepts](#wrapping-up-and-integrating-concepts)
    - [Creating the new `quotes` topic](#creating-the-new-quotes-topic)
    - [Python Producer for Kafka (Data Generation)](#python-producer-for-kafka-data-generation)
    - [Spring Boot Backend](#spring-boot-backend)
    - [React Frontend](#react-frontend)
    - [Docker Deployment](#docker-deployment)
5. [References](#references)

---

## How to run the project

1. **Start the services**:
```bash
docker compose up --build
```

2. **Wait for the services to start**:
   - Access the Kafdrop UI at [http://localhost:9009](http://localhost:9009).
   - Access the React frontend at [http://localhost:5173](http://localhost:5173).

3. **Run the Python producer (at `./lab5_4/kafka_quotes/kafka_quotes`)**:
```bash
poetry run python quotes_producer.py
```

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

---

## Wrapping-up and integrating concepts

In this exercise, I integrated what was missing for a full combination of **Kafka messaging**, **Spring Boot backend services**, **Relational DB**, **WebSocket communication**, and **React frontend** components to create a fully functional **movie/quote** system.

### Creating the new `quotes` topic

To create the new `quotes` topic, I used the following command:

```bash
docker exec lab05-kafka-1 kafka-topics --create --topic quotes --partitions 1 --replication-factor 1 --bootstrap-server kafka:9092
```

### **Python Producer for Kafka (Data Generation)**

#### Purpose: (`./lab5_4/kafka_quotes/kafka_quotes/quotes_producer.py`)
Created a Python script to **generate random quotes associated with movie data** every 5–10 seconds and send them to a Kafka topic named **`quotes`**.

#### Key Features:
- **Random Quote Generation**:
  - A predefined list of quotes is randomly selected for each message.
  - Each message contains a movie ID, title, release year, and a quote.

- **Kafka Producer**:
  - Configured with **`localhost:29092`** (in environment variables) as the Kafka broker.
  - Uses `json.dumps` to serialize the message in JSON format before sending.

- **Message Sending**:
  - Each message is sent to the Kafka topic **`quotes`**.
  - The producer ensures the system constantly receives new data for processing.

### **Spring Boot Backend**

The Spring Boot backend acts as the core processing unit, **consuming Kafka messages**, **storing data in a database**, and **relaying updates to the React frontend via `WebSocket`**.

#### Application Properties

In the `application.properties`, among other configurations, I configured the **Kafka consumer** settings to `deserialize` JSON messages into `Message` objects:

```properties
spring.kafka.bootstrap-servers: kafka:9092
spring.kafka.consumer.group-id=consumers_1
spring.kafka.consumer.auto-offset-reset: earliest
spring.kafka.consumer.key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
spring.kafka.consumer.value-deserializer: org.springframework.kafka.support.serializer.JsonDeserializer
spring.kafka.consumer.properties.spring.json.trusted.packages=*
spring.kafka.consumer.properties[spring.json.value.default.type]=ies.lab3.ex3.entity.Message
```

#### Kafka Consumer Service

The **`KafkaConsumerService`** listens to the Kafka topic **`quotes`**.

- **Message Processing**:
  - Each Kafka message (JSON) is **deserialized** into a `Message` object using **Spring Kafka's JsonDeserializer**.

```java
public class Message {
    // message = {
    //     "movieId": movie["id"],
    //     "movieTitle": movie["title"],
    //     "movieYear": movie["year"],
    //     "quote": quote
    // }

    private int movieId;
    private String movieTitle;
    private String movieYear;
    private String quote;

    public Message() {
    }

    public Message(int movieId, String movieTitle, String movieYear, String quote) {
        this.movieId = movieId;
        this.movieTitle = movieTitle;
        this.movieYear = movieYear;
        this.quote = quote;
    }
    
    ...
```
  - A `Movie` object is created or updated in the database.
  - A corresponding `Quote` is created and saved in the database.

- **Integration with WebSocket**:
  - The processed movie and quote are sent to WebSocket subscribers using the `MessageController` class.

```java
@KafkaListener(topics = "quotes", groupId = "consumers_1")
public void listen(Message message) {
    Movie movie = new Movie(message.getMovieTitle(), message.getMovieYear());
    movieService.createMovie(movie);

    Quote quote = new Quote(message.getQuote(), movie);
    quoteService.createQuote(quote);

    webSocketController.sendMovie(movie);
    webSocketController.sendQuote(quote);
}
```

#### WebSocket Configuration

The **`WebSocketConfig`** class enables WebSocket communication between the backend and the frontend.

- **Endpoints**:
  - **`/backend-ws`**: WebSocket endpoint **clients** (React frontend) **use to connect**.

```java
@Override
public void registerStompEndpoints(StompEndpointRegistry registry) {
    // Clients connect to WebSocket at "/backend-ws"
    registry.addEndpoint("/backend-ws").setAllowedOrigins("http://" + frontendIp + ":" + frontendPort);
}
```

  - **`/topic/movies`** and **`/topic/quotes`**: **Subscriptions** for receiving updates about movies and quotes.

- **Broker Setup**:
  - Uses Spring's SimpleBroker to route messages to the appropriate topics.

```java
@Override
public void configureMessageBroker(MessageBrokerRegistry config) {
    config.enableSimpleBroker("/topic"); // Broadcast to "/topic/movies" and "/topic/quotes"
}
```

#### Message Controller

The **`MessageController`** class uses Spring's `SimpMessagingTemplate` to send real-time updates to WebSocket clients.

- **Broadcasting Updates**:
  - When a new movie or quote is processed, it is sent to all WebSocket clients subscribed to the respective topics.

```java
public void sendMovie(Movie movie) {
    messagingTemplate.convertAndSend("/topic/movies", movie);
}

public void sendQuote(Quote quote) {
    messagingTemplate.convertAndSend("/topic/quotes", quote);
}
```

### **React Frontend**

The React frontend provides a user interface to display the latest movies and quotes.

#### Real-Time Updates with WebSocket -> API Consumer Services

- **WebSocket Client**:
  - Uses the `@stomp/stompjs` library to connect to the backend WebSocket endpoint (**`ws://localhost:8080/backend-ws`**).
  - Subscribes to **`/topic/movies`** and **`/topic/quotes`** to receive real-time updates.

- **React State Management**:
  - Updates the state with the **latest 5 movies/quotes** as they arrive.
  - **This ensures the frontend reflects the most recent data without requiring a manual refresh**.

```javascript
stompClient?.subscribe("/topic/movies", (message) => {
    const newMovie = JSON.parse(message.body);
    setMovies((prevMovies) => {
        const updatedMovies = [...prevMovies, newMovie];
        return updatedMovies.slice(-5); // Keep only the last 5 movies
    });
});
```

#### Movie and Quote Pages

- **Display Latest Data**:
  - The **`MoviePage`** and **`QuotePage`** components fetch existing movies/quotes and subscribe to WebSocket updates.

- **Dynamic UI Updates**:
  - The pages automatically update to show the latest 5 movies and quotes, leveraging WebSocket real-time communication.


### **Docker Deployment**

```yaml
name: lab05
services:

  kafdrop:
    image: obsidiandynamics/kafdrop:4.0.2
    ports:
      - "${KAFDROP_LOCAL_PORT}:${KAFDROP_CONTAINER_PORT}"
    environment:
      KAFKA_BROKERCONNECT: "kafka:${KAFKA_BROKER_PORT}"
      SERVER_SERVLET_CONTEXTPATH: "${KAFDROP_CONTEXT_PATH}"
    networks:
      - ${NETWORK_NAME}

  mysqldb:
    image: mysql/mysql-server:5.7
    environment:
      MYSQL_ROOT_PASSWORD: ${MYSQL_ROOT_PASSWORD}
      MYSQL_DATABASE: ${MYSQL_DATABASE}
      MYSQL_USER: ${MYSQL_USER}
      MYSQL_PASSWORD: ${MYSQL_PASSWORD}
    ports:
      - "${DATABASE_LOCAL_PORT}:${DATABASE_CONTAINER_PORT}"
    restart: on-failure
    volumes:
      - mysql_data:/var/lib/mysql
    networks:
      - ${NETWORK_NAME}

  zookeeper:
    image: confluentinc/cp-zookeeper:7.4.4
    environment:
      ZOOKEEPER_CLIENT_PORT: ${ZOOKEEPER_CONTAINER_PORT}
      ZOOKEEPER_TICK_TIME: ${ZOOKEEPER_TICK_TIME}
    ports:
      - "${ZOOKEEPER_LOCAL_PORT}:${ZOOKEEPER_CONTAINER_PORT}"
    networks:
      - ${NETWORK_NAME}
  
  kafka:
    image: confluentinc/cp-kafka:7.4.4
    depends_on:
      - zookeeper
    ports:
      - "${KAFKA_LOCAL_PORT}:${KAFKA_CONTAINER_PORT}"
    environment:
      KAFKA_BROKER_ID: ${KAFKA_BROKER_ID}
      KAFKA_ZOOKEEPER_CONNECT: "zookeeper:${ZOOKEEPER_CONTAINER_PORT}"
      KAFKA_ADVERTISED_LISTENERS: "PLAINTEXT://kafka:${KAFKA_BROKER_PORT},PLAINTEXT_HOST://${KAFKA_IP}:${KAFKA_CONTAINER_PORT}"
      KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: "PLAINTEXT:PLAINTEXT,PLAINTEXT_HOST:PLAINTEXT"
      KAFKA_INTER_BROKER_LISTENER_NAME: "PLAINTEXT"
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: ${KAFKA_REPLICATION_FACTOR}
      KAFKA_LOG_RETENTION_MS: ${KAFKA_RETENTION_MS}
      KAFKA_LOG_RETENTION_CHECK_INTERVAL_MS: ${KAFKA_RETENTION_CHECK_INTERVAL_MS}
    networks:
      - ${NETWORK_NAME}

  app_container:
    depends_on:
      - mysqldb
      - kafka
    build: 
      context: ${BACKEND_CONTEXT_PATH}
      dockerfile: Dockerfile
    ports:
      - "${BACKEND_LOCAL_PORT}:${BACKEND_CONTAINER_PORT}"
    environment:
      FRONTEND_IP: ${FRONTEND_IP}
      FRONTEND_PORT: ${FRONTEND_CONTAINER_PORT}
    restart: on-failure
    volumes:
      - .m2:/root/.m2    # persist Maven dependencies
    networks:
      - ${NETWORK_NAME}

  react_app:
    depends_on:
      - app_container
    build:
      context: ${FRONTEND_CONTEXT_PATH}
      dockerfile: Dockerfile
    ports:
      - "${FRONTEND_LOCAL_PORT}:${FRONTEND_CONTAINER_PORT}"
    environment:
      BACKEND_IP: ${BACKEND_IP}
      BACKEND_PORT: ${BACKEND_CONTAINER_PORT}
    restart: on-failure
    networks:
      - ${NETWORK_NAME}

  # The generator will be a third-party service that will produce messages to the Kafka topic (run separately)
  # generator:
  #   depends_on:
  #     - kafka
  #     - react_app
  #   build:
  #     context: ${GENERATOR_CONTEXT_PATH}
  #     dockerfile: Dockerfile
  #   environment:
  #     BROKER_IP: kafka
  #     BROKER_PORT: ${KAFKA_CONTAINER_PORT}
  #     TOPIC_NAME: ${KAFKA_TOPIC}
  #   command: >
  #     python /app/wait_for_kafka.py kafka ${KAFKA_CONTAINER_PORT} && 
  #     poetry run python kafka_quotes/quotes_producer.py
  #   networks:
  #     - ${NETWORK_NAME}

networks:
  app_network:

volumes:
  mysql_data:
```

---

## References

- [Poetry](https://python-poetry.org/)
- [Kafka Documentation](https://kafka.apache.org/documentation/)
- [How to implement a Serializer in Spring Boot to deserialize the message produced](https://howtodoinjava.com/kafka/spring-boot-jsonserializer-example/)
- [KafkaTemplate](https://docs.spring.io/spring-kafka/reference/kafka/sending-messages.html)
