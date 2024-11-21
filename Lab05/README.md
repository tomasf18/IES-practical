# 112981

# Lab 4: Objective of this lab

- Develop web projects with Spring Boot.
- Create and persist entities into a relational database using Spring Data.
- Deploy Spring Boot application in Docker.

## Table of Contents



## Configure Apache Kafka with Docker
### 1_f

#### What happens if you open multiple consumer terminals?
- If I open multiple consumer terminals and connect them to the same topic:
  - Each consumer terminal will receive all the messages sent to the topic. This is because, by default, consumers are independent, so they all process the same stream of data.

#### And if you open multiple terminals with producers? 
- If I open multiple producer terminals and send messages to the same topic, all messages from all producers will be appended to the topic's log. Kafka is designed to handle high-throughput and concurrent writes, so multiple producers writing to the same topic works seamlessly.

#### What happens with old messages? 
- **Retention Policy:** Kafka does not automatically delete messages after they are consumed. Instead, it retains them based on the topic's retention policy (`KAFKA_LOG_RETENTION_MS` in the Docker Compose file). In this setup, the retention is configured to keep messages for 10 seconds (`KAFKA_LOG_RETENTION_MS: 10000`).
- **Effect on Consumers:**
  - If a new consumer connects to the topic within the retention window (10 seconds), it can use the `--from-beginning` flag to read all messages, including old ones.
  - If the retention period has elapsed, old messages are deleted, and new consumers will not see those messages.

#### Conclusions
- Kafka allows multiple producers and consumers to interact with a topic efficiently.
- Messages are retained based on the configured retention period, not consumption status.