import sys
import types

# Mock the 'kafka.vendor.six.moves' module to bypass missing dependency issues
m = types.ModuleType('kafka.vendor.six.moves', 'Mock module')
setattr(m, 'range', range)
sys.modules['kafka.vendor.six.moves'] = m

from kafka import KafkaProducer
import json

nMec = 112981

# Kafka Configuration
bootstrap_servers = 'localhost:29092'  # Kafka broker address
topic_name = f'lab05_{nMec}'

# Create a Kafka producer instance
producer = KafkaProducer(
    bootstrap_servers=bootstrap_servers,
    value_serializer=lambda value: json.dumps(value).encode('utf-8')  # Serialize messages as JSON
)

# Generate and send messages
a, b = 0, 1  # Fibonacci sequence initialization
generated_number = 1

while generated_number <= nMec:
    # Create the message
    message = {
        'nMec': str(nMec),
        'generatedNumber': generated_number,
        'type': 'fibonacci'
    }
    
    # Send the message to Kafka
    producer.send(topic_name, value=message)
    print(f"Sent message: {message}")
    
    # Generate the next number in the Fibonacci sequence
    a, b = b, a + b
    generated_number = b

# Close the producer
producer.close()
