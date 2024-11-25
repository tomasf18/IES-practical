import socket
import time
import sys

def wait_for_kafka(host, port, timeout=300):
    start_time = time.time()
    while True:
        try:
            # Try connecting to Kafka
            with socket.create_connection((host, port), timeout=10):
                print(f"Kafka {host}:{port} is available!")
                break
        except (socket.timeout, socket.error):
            # Kafka is not available, check if we've timed out
            if time.time() - start_time > timeout:
                print(f"Timed out waiting for Kafka {host}:{port}")
                sys.exit(1)
            print(f"Waiting for Kafka {host}:{port}...")
            time.sleep(1)

if __name__ == "__main__":
    host = sys.argv[1]
    port = int(sys.argv[2])
    wait_for_kafka(host, port)
