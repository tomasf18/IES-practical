import sys
import sys, types

m = types.ModuleType('kafka.vendor.six.moves', 'Mock module')
setattr(m, 'range', range)
sys.modules['kafka.vendor.six.moves'] = m

from kafka import KafkaConsumer, TopicPartition

nMec = 112981

# Kafka Configuration
bootstrap_servers = 'localhost:29092'  # Kafka Broker address
topic_name = f'lab05_{nMec}'  #
group_id = 'consumers_1'  # Choose a unique group ID for your consumer

# Create a Kafka consumer instance
consumer = KafkaConsumer(
    topic_name,
    bootstrap_servers=bootstrap_servers,
    group_id=group_id,
    enable_auto_commit=False,     # Disable auto-commit, meaning that the consumer will manually commit offsets, 
                                # this means that running the consumer again will consume the same messages within the retention period
    auto_offset_reset='earliest'  # Start consuming from the beginning of the topic if no offset is stored
)
PARTITIONS = []
for partition in consumer.partitions_for_topic(topic_name):
    PARTITIONS.append(TopicPartition(topic_name, partition))
    
end_offsets = consumer.end_offsets(PARTITIONS)
print(end_offsets)

# Consume messages
try:
    for message in consumer:
        print(f"Received message: {message.value.decode('utf-8')}")  # Decode message from bytes
except KeyboardInterrupt:
    print("\nConsumer interrupted by user. Closing...")
finally:
    consumer.close()
    print("Consumer closed.")