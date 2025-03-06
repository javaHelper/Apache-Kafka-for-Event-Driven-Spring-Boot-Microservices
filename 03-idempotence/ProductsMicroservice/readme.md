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

- If spring.kafka.producer.properties.enable.idempotence=true and spring.kafka.producer.acks=all, 
then max.in.flight.requests.per.connection=5 is what you can use.
if you used max.in.flight.requests.per.connection=6, then code will not work.