# Use an official Python 3.12 runtime as the base image
FROM python:3.12-slim

# Set the working directory inside the container
WORKDIR /app

# Copy Poetry configuration files first
COPY pyproject.toml poetry.lock ./

# Install Poetry
RUN pip install --no-cache-dir poetry

# Install dependencies using Poetry
RUN poetry config virtualenvs.create false && poetry install --no-interaction --no-ansi

# Copy the entire project into the container
COPY . .

# Copy the wait-for-kafka.py script into the container
COPY wait_for_kafka.py /app/wait_for_kafka.py

# Make sure the script is executable
RUN chmod +x /app/wait_for_kafka.py

# Command to run the application (wait for Kafka first, then run the producer)
CMD ["python", "/app/wait_for_kafka.py", "kafka", "29092", "&&", "poetry", "run", "python", "kafka_quotes/quotes_producer.py"]
