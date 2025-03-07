# Rebalancing

- No of partitions = No of consumers

Case: 1
- If No of kafka topic partition is 3, and only one consumer instance is started all partitions will be assigned to him

````
[ntainer#0-0-C-1] o.s.k.l.KafkaMessageListenerContainer    : product-created-events: partitions assigned: [products-created-events-topic-0, products-created-events-topic-1, products-created-events-topic-2]
````

Case:2
- If No of kafka topic partition is 3, and two consumer instances are started, 1 consumer will get 1 partition and other consumer will get 2 partitions

Consumer 1 has assigned partitioned 2  
````
product-created-events: partitions assigned: [products-created-events-topic-2]
````

Consumer 2 has assigned partition 0 & 1 
````
product-created-events: partitions assigned: [products-created-events-topic-0, products-created-events-topic-1]
````

Clearly only 1 consumer has received the message.

````
Received a new event: ProductCreatedEvent(productId=b444ed17-47e1-499d-9a6b-d23478f4a0a5, title=Iphone 14, price=1000, quantity=10)
Received Response from remote service :200
````