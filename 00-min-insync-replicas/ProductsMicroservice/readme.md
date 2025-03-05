#

````sh
kafka-topics --create --topic products-created-events-topic --partitions 3 --replication-factor 3 --bootstrap-server localhost:9092
Created topic products-created-events-topic.
````

````
kafka-topics --describe --topic products-created-events-topic --bootstrap-server localhost:9092
Topic: products-created-events-topic	TopicId: XCPJxhVbRsG8nfJbcytHDg	PartitionCount: 3	ReplicationFactor: 3	Configs: 
Topic: products-created-events-topic	Partition: 0	Leader: 1	Replicas: 1,2,3	Isr: 1,2,3	Offline: 
Topic: products-created-events-topic	Partition: 1	Leader: 2	Replicas: 2,3,1	Isr: 2,3,1	Offline: 
Topic: products-created-events-topic	Partition: 2	Leader: 3	Replicas: 3,1,2	Isr: 3,1,2	Offline:
````

````shell
curl --location 'http://localhost:62885/products' \
--header 'Content-Type: application/json' \
--data '{
    "title":"Iphone 13",
    "price": 800,
    "quantity": 10
}'
````

````
b69b71be-5566-4958-b060-0d2800fbbbc7
````

````shell
kafka-console-consumer --bootstrap-server localhost:9092 --topic products-created-events-topic --property "parse.key=true" --property "key.separator=:" --from-beginning --property print.key=true
77dade93-337c-48fc-b007-21422bb2f444:{"productId":"77dade93-337c-48fc-b007-21422bb2f444","title":"Iphone 13","price":800,"quantity":10}
````

-----------

````
kafka-topics --create --topic insync-topic --partitions 3 --replication-factor 3 --bootstrap-server localhost:9092 --config min.insync.replicas=2 
````

````
kafka-topics --describe --topic insync-topic --bootstrap-server localhost:9092 
Topic: insync-topic	TopicId: 4_XhvSo2RsK1u4za9Rh1Qg	PartitionCount: 3	ReplicationFactor: 3	Configs: min.insync.replicas=2
	Topic: insync-topic	Partition: 0	Leader: 3	Replicas: 3,1,2	Isr: 3,1,2	Offline: 
	Topic: insync-topic	Partition: 1	Leader: 1	Replicas: 1,2,3	Isr: 1,2,3	Offline: 
	Topic: insync-topic	Partition: 2	Leader: 2	Replicas: 2,3,1	Isr: 2,3,1	Offline: 
````

````
kafka-topics --create --topic topic2 --partitions 3 --replication-factor 3 --bootstrap-server localhost:9092,localhost:9094
````

````
kafka-configs --bootstrap-server localhost:9092 --alter --entity-type topics --entity-name topic2 --add-config min.insync.replicas=2 
````

````
kafka-topics --describe --topic topic2  --bootstrap-server localhost:9092
Topic: topic2	TopicId: 2KQMZt7HSqOKDNM6SVXxeA	PartitionCount: 3	ReplicationFactor: 3	Configs: min.insync.replicas=2
	Topic: topic2	Partition: 0	Leader: 2	Replicas: 2,3,1	Isr: 2,3,1	Offline: 
	Topic: topic2	Partition: 1	Leader: 3	Replicas: 3,1,2	Isr: 3,1,2	Offline: 
	Topic: topic2	Partition: 2	Leader: 1	Replicas: 1,2,3	Isr: 1,2,3	Offline: 

````

- Note - min.insync.replicas=2 that means at-least two ISR should be running

Case-1: Now down 1 node

````
docker ps 
CONTAINER ID   IMAGE                  COMMAND                  CREATED        STATUS        PORTS                              NAMES
efd61d88dc8b   bitnami/kafka:latest   "/opt/bitnami/script…"   12 hours ago   Up 12 hours   9092/tcp, 0.0.0.0:9094->9094/tcp   apache-kafka-for-event-driven-spring-boot-microservices-kafka-2-1
9d0e3f850f4f   bitnami/kafka:latest   "/opt/bitnami/script…"   12 hours ago   Up 12 hours   0.0.0.0:9092->9092/tcp             apache-kafka-for-event-driven-spring-boot-microservices-kafka-1-1
````

Now hope is code still works

![Screenshot 2025-03-05 at 10.18.28 PM.png](../../Desktop/Screenshot%202025-03-05%20at%2010.18.28%E2%80%AFPM.png)

Case-2: Stop one more server, only 1 server is up and expectation is that code should fail

code will keep doing retry for 

````
2025-03-05T22:21:06.945+05:30  WARN 84406 --- [ad | producer-1] o.a.k.clients.producer.internals.Sender  : [Producer clientId=producer-1] Got error produce response with correlation id 523 on topic-partition topic2-2, retrying (2147483133 attempts left). Error: NOT_ENOUGH_REPLICAS
2025-03-05T22:21:07.066+05:30  WARN 84406 --- [ad | producer-1] o.a.k.clients.producer.internals.Sender  : [Producer clientId=producer-1] Got error produce response with correlation id 524 on topic-partition topic2-2, retrying (2147483132 attempts left). Error: NOT_ENOUGH_REPLICAS
2025-03-05T22:21:07.172+05:30  WARN 84406 --- [ad | producer-1] o.a.k.clients.producer.internals.Sender  : [Producer clientId=producer-1] Got error produce response with correlation id 525 on topic-partition topic2-2, retrying (2147483131 attempts left). Error: NOT_ENOUGH_REPLICAS
````