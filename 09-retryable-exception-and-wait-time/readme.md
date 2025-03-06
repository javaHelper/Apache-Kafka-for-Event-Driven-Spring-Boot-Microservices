#

Happy path

````
curl --location 'http://localhost:8080/products' \
--header 'Content-Type: application/json' \
--data '{
    "title":"Iphone 13",
    "price": 800,
    "quantity": 10
}'
````

````
e1aa1598-cbb8-4788-9599-4cbb21372662
````

````
2025-03-06T18:58:01.732+05:30  INFO 28114 --- [ntainer#0-0-C-1] o.a.k.c.c.internals.ConsumerCoordinator  : [Consumer clientId=consumer-product-created-events-1, groupId=product-created-events] Setting offset for partition products-created-events-topic-0 to the committed offset FetchPosition{offset=4, offsetEpoch=Optional.empty, currentLeader=LeaderAndEpoch{leader=Optional[localhost:9096 (id: 3 rack: null)], epoch=6}}
2025-03-06T18:58:01.733+05:30  INFO 28114 --- [ntainer#0-0-C-1] o.s.k.l.KafkaMessageListenerContainer    : product-created-events: partitions assigned: [products-created-events-topic-0, products-created-events-topic-1, products-created-events-topic-2]
2025-03-06T18:58:21.017+05:30  INFO 28114 --- [ntainer#0-0-C-1] c.a.w.e.h.ProductCreatedEventHandler     : Received a new event: ProductCreatedEvent(productId=e1aa1598-cbb8-4788-9599-4cbb21372662, title=Iphone 13, price=800, quantity=10)
2025-03-06T18:58:39.420+05:30  INFO 28114 --- [ntainer#0-0-C-1] c.a.w.e.h.ProductCreatedEventHandler     : Received Response from remote service :200
````

# 

when mockservice is down

````
curl --location 'http://localhost:8080/products' \
--header 'Content-Type: application/json' \
--data '{
    "title":"Iphone 14",
    "price": 1000,
    "quantity": 10
}'
````

````
prateekashtikar@Prateeks-MBP mockservice % kafka-console-consumer --bootstrap-server localhost:9092 --topic products-created-events-topic.DLT --property print.key=true --property print.value=true --from-beginning
1	"e2prZHNqa2Rzamtkc30="
7057bfff-4297-424e-829d-bc75a57b3b2d	{"productId":"7057bfff-4297-424e-829d-bc75a57b3b2d","title":"Iphone 14","price":1000,"quantity":10}
````