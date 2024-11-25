import os
import sys
import types
import random
import time
from dotenv import load_dotenv

load_dotenv()

# Mock the 'kafka.vendor.six.moves' module to bypass missing dependency issues
m = types.ModuleType('kafka.vendor.six.moves', 'Mock module')
setattr(m, 'range', range)
sys.modules['kafka.vendor.six.moves'] = m

from kafka import KafkaProducer
import json

# Kafka Configuration
broker_ip = os.getenv('BROKER_IP') or 'localhost'
broker_port = os.getenv('BROKER_PORT') or '29092'
topic_name = os.getenv('TOPIC_NAME') or 'quotes'
bootstrap_servers = f'{broker_ip}:{broker_port}' 

print(f"Broker IP: {bootstrap_servers}")
print (f"Topic Name: {topic_name}")

# Create a Kafka producer instance
producer = KafkaProducer(
    bootstrap_servers=bootstrap_servers,
    value_serializer=lambda value: json.dumps(value).encode('utf-8')  # Serialize messages as JSON
)

# List of possible quotes (you can expand this list)
quotes_list = ["The Matrix has you.","Why so serious?","The truth is out there.","You talking to me?","Just keep swimming.","I'm your Huckleberry.","I see dead people.","I'll be back.","You can't handle the truth!","I feel the need, the need for speed.","May the force be with you.","You're gonna need a bigger boat.","Here's looking at you, kid.","You can't handle the truth!","Houston, we have a problem.","I am your father.","I drink your milkshake!","You're gonna need a bigger boat.","I am big!","Just keep swimming.","Just keep swimming.","La-dee-da, la-dee-da.","Love you to the moon and back.","Love you three thousand.",]

i = 1

# Function to generate and send random quotes for movies
def generate_and_send_quote(i):
    movie = {"id": i, "title": f'Movie {i % 500}', "year": random.randint(2020, 2024)}
    quote = random.choice(quotes_list) 
    
    # Create a message to send to Kafka
    message = {
        "movieId": movie["id"],
        "movieTitle": movie["title"],
        "movieYear": movie["year"],
        "quote": quote
    }
    
    # Send the message to Kafka
    producer.send(topic_name, value=message)
    print(f"Sent message: {message}")

# Run the process every 5 to 10 seconds
try:
    while True:
        generate_and_send_quote(i)
        i += 1
        time.sleep(random.randint(5, 10))
except KeyboardInterrupt:
    print("Process interrupted. Closing producer.")

# Close the producer when done
producer.close()
