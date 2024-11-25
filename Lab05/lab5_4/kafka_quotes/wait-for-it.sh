#!/bin/bash
# wait-for-it.sh: Wait for a service to be available

host=$1
port=$2

while ! nc -z $host $port; do
  echo "Waiting for $host:$port to be available..."
  sleep 1
done

echo "$host:$port is available!"
exec "$@"
