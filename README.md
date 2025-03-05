# Apache-Kafka-for-Event-Driven-Spring-Boot-Microservices


docker-compose -f docker-compose.yml --env-file environment.env up

```sh
version: "3.8"
services:
  kafka-1:
    image: bitnami/kafka:latest
    ports:
      - "9092:9092"
    environment:
      - KAFKA_CFG_NODE_ID=1
      - KAFKA_KRAFT_CLUSTER_ID=WnLkTHhkQaiJbwP8FClPhw
      - KAFKA_CFG_PROCESS_ROLES=controller,broker
      - KAFKA_CFG_CONTROLLER_QUORUM_VOTERS=1@kafka-1:9091
      - KAFKA_CFG_LISTENERS=PLAINTEXT://:9090,CONTROLLER://:9091,EXTERNAL://:9092
      - KAFKA_CFG_ADVERTISED_LISTENERS=PLAINTEXT://kafka-1:9090,EXTERNAL://${HOSTNAME:-localhost}:9092
      - KAFKA_CFG_LISTENER_SECURITY_PROTOCOL_MAP=CONTROLLER:PLAINTEXT,EXTERNAL:PLAINTEXT,PLAINTEXT:PLAINTEXT
      - KAFKA_CFG_CONTROLLER_LISTENER_NAMES=CONTROLLER
      - KAFKA_CFG_INTER_BROKER_LISTENER_NAME=PLAINTEXT
    volumes:
      - /Users/prats/kafka/docker-compose/volumes/volumes/server-1:/bitnami/kafka
```
