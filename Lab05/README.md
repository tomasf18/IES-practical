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


## 2
RETENTION TIME = 10s, quando vou ao broker ver as mensagens publicadas no topico, so consigo ver aquelas que foram publicadas nos ultimos 10s. As mensagens antigas sao apagadas automaticamente apos esse periodo de tempo. O que significa que nem os consumidores conseguem aceder a essas mensagens antigas, apenas ao seu offset (nº de mensagens que ja foram produzidas no topico).

### **What is the last message?**

The last message produced by the Kafka topic "lab05_112981" is:

```json
{"nMec": "112981", "generatedNumber": 75025, "type": "fibonacci"}
```

This corresponds to the largest Fibonacci number less than or equal to `112981`, which matches the `nMec` value.
Also, corresponds to the last message produced for the sequence.

---

### **If you run the consumer multiple times, does it read all the messages? Why?**

No, the consumer does **not necessarily read all messages again** if I run it multiple times. 
The reason lies in how **consumer commit** and **retention time** work in Kafka:

1. **Consumer Commit**:
   - Kafka tracks the offset (position) for each consumer group in a topic. If the consumer has the `enable_auto_commit` option enabled (default), it will periodically save its progress (offset) back to Kafka.
   - On subsequent runs, the consumer starts from the last committed offset, skipping already-read messages.

2. **Retention Time**:
   - The topic's messages are retained for a limited time (`KAFKA_LOG_RETENTION_MS: 10000`, i.e., 10 seconds). If messages expire, they will no longer be available for the consumer to read. 
   - And if I go to the Kafka Broker and try to see the messages on the topic, I can only see the messages published in the last <`retention_time`> seconds.

So, if:
- the consumer commits offsets, it will only consume unread messages on subsequent runs;
- messages are removed from the topic after the retention period, they are no longer available to any consumer.