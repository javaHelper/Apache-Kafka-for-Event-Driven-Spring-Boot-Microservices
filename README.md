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

# Multi Node Cluster Setup

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
      - KAFKA_CFG_CONTROLLER_QUORUM_VOTERS=1@kafka-1:9091,2@kafka-2:9091,3@kafka-3:9091
      - KAFKA_CFG_LISTENERS=PLAINTEXT://:9090,CONTROLLER://:9091,EXTERNAL://:9092
      - KAFKA_CFG_ADVERTISED_LISTENERS=PLAINTEXT://kafka-1:9090,EXTERNAL://${HOSTNAME:-localhost}:9092
      - KAFKA_CFG_LISTENER_SECURITY_PROTOCOL_MAP=CONTROLLER:PLAINTEXT,EXTERNAL:PLAINTEXT,PLAINTEXT:PLAINTEXT
      - KAFKA_CFG_CONTROLLER_LISTENER_NAMES=CONTROLLER
      - KAFKA_CFG_INTER_BROKER_LISTENER_NAME=PLAINTEXT
    volumes:
      - /Users/prats/kafka/docker-compose/volumes/server-1:/bitnami/kafka

  kafka-2:
    image: bitnami/kafka:latest
    ports:
      - "9094:9094"
    environment:
      - KAFKA_CFG_NODE_ID=2
      - KAFKA_KRAFT_CLUSTER_ID=WnLkTHhkQaiJbwP8FClPhw
      - KAFKA_CFG_PROCESS_ROLES=controller,broker
      - KAFKA_CFG_CONTROLLER_QUORUM_VOTERS=1@kafka-1:9091,2@kafka-2:9091,3@kafka-3:9091
      - KAFKA_CFG_LISTENERS=PLAINTEXT://:9090,CONTROLLER://:9091,EXTERNAL://:9094
      - KAFKA_CFG_ADVERTISED_LISTENERS=PLAINTEXT://kafka-2:9090,EXTERNAL://${HOSTNAME:-localhost}:9094
      - KAFKA_CFG_LISTENER_SECURITY_PROTOCOL_MAP=CONTROLLER:PLAINTEXT,EXTERNAL:PLAINTEXT,PLAINTEXT:PLAINTEXT
      - KAFKA_CFG_CONTROLLER_LISTENER_NAMES=CONTROLLER
      - KAFKA_CFG_INTER_BROKER_LISTENER_NAME=PLAINTEXT
    volumes:
      - /Users/prats/kafka/docker-compose/volumes/server-2:/bitnami/kafka

  kafka-3:
    image: bitnami/kafka:latest
    ports:
      - "9096:9096"
    environment:
      - KAFKA_CFG_NODE_ID=3
      - KAFKA_KRAFT_CLUSTER_ID=WnLkTHhkQaiJbwP8FClPhw
      - KAFKA_CFG_PROCESS_ROLES=controller,broker
      - KAFKA_CFG_CONTROLLER_QUORUM_VOTERS=1@kafka-1:9091,2@kafka-2:9091,3@kafka-3:9091
      - KAFKA_CFG_LISTENERS=PLAINTEXT://:9090,CONTROLLER://:9091,EXTERNAL://:9096
      - KAFKA_CFG_ADVERTISED_LISTENERS=PLAINTEXT://kafka-3:9090,EXTERNAL://${HOSTNAME:-localhost}:9096
      - KAFKA_CFG_LISTENER_SECURITY_PROTOCOL_MAP=CONTROLLER:PLAINTEXT,EXTERNAL:PLAINTEXT,PLAINTEXT:PLAINTEXT
      - KAFKA_CFG_CONTROLLER_LISTENER_NAMES=CONTROLLER
      - KAFKA_CFG_INTER_BROKER_LISTENER_NAME=PLAINTEXT
    volumes:
      - /Users/prats/kafka/docker-compose/volumes/server-3:/bitnami/kafka
```


# Create Topics

```sh
@Prateeks-MBP ~ % kafka-topics --create --topic topic1 --partitions 3 --replication-factor 3 --bootstrap-server localhost:9092,localhost:9094
Created topic topic1.

@Prateeks-MBP ~ % kafka-topics --create --topic topic2 --partitions 3 --replication-factor 3 --bootstrap-server localhost:9092,localhost:9094
Created topic topic2.
```

# List all topics

```
% kafka-topics --list --bootstrap-server localhost:9092
topic1
topic2
```

# Describe 

```
% kafka-topics --describe --bootstrap-server localhost:9092
Topic: topic1	TopicId: RAl7vy8aQSatGI2JCVSiwg	PartitionCount: 3	ReplicationFactor: 3	Configs: 
	Topic: topic1	Partition: 0	Leader: 2	Replicas: 2,3,1	Isr: 2,3,1	Offline: 
	Topic: topic1	Partition: 1	Leader: 3	Replicas: 3,1,2	Isr: 3,1,2	Offline: 
	Topic: topic1	Partition: 2	Leader: 1	Replicas: 1,2,3	Isr: 1,2,3	Offline: 
Topic: topic2	TopicId: UjkqeZs5Qa2VhXqBBpnfUA	PartitionCount: 3	ReplicationFactor: 3	Configs: 
	Topic: topic2	Partition: 0	Leader: 1	Replicas: 1,2,3	Isr: 1,2,3	Offline: 
	Topic: topic2	Partition: 1	Leader: 2	Replicas: 2,3,1	Isr: 2,3,1	Offline: 
	Topic: topic2	Partition: 2	Leader: 3	Replicas: 3,1,2	Isr: 3,1,2	Offline: 

```

# Delete Topic

```sh
@Prateeks-MBP ~ % kafka-topics --delete --topic topic2 --bootstrap-server localhost:9092

@Prateeks-MBP ~ % kafka-topics --list --bootstrap-server localhost:9092             
topic1
```

# Produce Messages

```
kafka-console-producer --bootstrap-server localhost:9092,localhost:9094 --topic my-topic
>Hello World
[2025-03-05 11:00:24,344] WARN [Producer clientId=console-producer] Error while fetching metadata with correlation id 8 : {my-topic=UNKNOWN_TOPIC_OR_PARTITION} (org.apache.kafka.clients.NetworkClient)
>Hello world 2
>    
```

# Produce Message with key

```
@Prateeks-MBP ~ % kafka-console-producer --bootstrap-server localhost:9092,localhost:9094 --topic my-topic --property "parse.key=true" --property "key.separator=:"
>firstName:Prateek
>lastName:Ninawe
>age:38
>
```

----------

# Consume Message

If you spin up multiple consumers, all the consumer (shell) will get the message messages, if you have 4 consumers all consumer will get all messages. 

```
@Prateeks-MBP ~ % kafka-console-consumer --topic my-topic --from-beginning --bootstrap-server localhost:9092
Hello World
Hello world 2
Prateek
Ninawe
38

```


# Consume key-value pair messages

```
kafka-console-producer --bootstrap-server localhost:9092,localhost:9094 --topic my-topic --property "parse.key=true" --property "key.separator=:"
>firstName:shrutika
>lastName:Ninawe
>age:40
>
```

```
kafka-console-consumer --bootstrap-server localhost:9092 --topic my-topic --property print.key=true
firstName	shrutika
lastName	Ninawe
age	40
```

# How to print both key & value?

```
kafka-console-consumer --bootstrap-server localhost:9092 --topic my-topic --property print.key=true --property print.value=true --from-beginning
null	Hello World
null	Hello world 2
firstName	Prateek
lastName	Ninawe
age	38
Hello	World
Spring	Action
firstName	Shrutika
lastName	Ninawe
firstName	shrutika
lastName	Ninawe
age	40

```

# Hide value and print key only

```
kafka-console-consumer --bootstrap-server localhost:9092 --topic my-topic --property print.key=true --property print.value=false --from-beginning
null
null
firstName
lastName
age
Hello
Spring
firstName
lastName
firstName
lastName
age

```
----------------

```
kafka-topics --create --topic messages-order --partitions 3 --replication-factor 3 --bootstrap-server localhost:9092
Created topic messages-order.
```

```
kafka-console-producer --topic messages-order --bootstrap-server localhost:9092 --property "parse.key=true" --property "key.separator=:"
>1:First Message
>2:Second Message
>3:Third Message
>4:Fourth Message
>5:Fifth Message
>a:a
>b:B
>c:c
>d:d

```

```
kafka-console-consumer --bootstrap-server localhost:9092 --topic messages-order --property "parse.key=true" --property "key.separator=:" --from-beginning --property print.key=true
1:First Message
1:Second Message
1:Third Message
1:Fourth Message
1:Sixth Message
a:a
b:b
c:c
d:d
e:e
f:f

```
