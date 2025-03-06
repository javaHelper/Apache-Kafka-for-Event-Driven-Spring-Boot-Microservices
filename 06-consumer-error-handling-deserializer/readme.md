#

````
kafka-console-producer --bootstrap-server localhost:9092 --topic products-created-events-topic --property "parse.key=true" --property "key.separator=:"
>1: {sfdasdasds}
````

````
2025-03-06T16:58:10.265+05:30  INFO 664 --- [ntainer#0-0-C-1] o.s.k.l.KafkaMessageListenerContainer    : product-created-events: partitions assigned: [products-created-events-topic-0, products-created-events-topic-1, products-created-events-topic-2]
2025-03-06T16:58:10.324+05:30 ERROR 664 --- [ntainer#0-0-C-1] o.s.kafka.listener.DefaultErrorHandler   : Backoff FixedBackOff{interval=0, currentAttempts=1, maxAttempts=0} exhausted for products-created-events-topic-0@1

org.springframework.kafka.listener.ListenerExecutionFailedException: Listener failed
	at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.decorateException(KafkaMessageListenerContainer.java:2930) ~[spring-kafka-3.1.0.jar:3.1.0]
	at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.checkDeser(KafkaMessageListenerContainer.java:2978) ~[spring-kafka-3.1.0.jar:3.1.0]
	at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.invokeOnMessage(KafkaMessageListenerContainer.java:2830) ~[spring-kafka-3.1.0.jar:3.1.0]
	at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.lambda$doInvokeRecordListener$56(KafkaMessageListenerContainer.java:2753) ~[spring-kafka-3.1.0.jar:3.1.0]
	at io.micrometer.observation.Observation.observe(Observation.java:565) ~[micrometer-observation-1.12.0.jar:1.12.0]
	at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.doInvokeRecordListener(KafkaMessageListenerContainer.java:2751) ~[spring-kafka-3.1.0.jar:3.1.0]
	at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.doInvokeWithRecords(KafkaMessageListenerContainer.java:2604) ~[spring-kafka-3.1.0.jar:3.1.0]
	at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.invokeRecordListener(KafkaMessageListenerContainer.java:2490) ~[spring-kafka-3.1.0.jar:3.1.0]
	at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.invokeListener(KafkaMessageListenerContainer.java:2132) ~[spring-kafka-3.1.0.jar:3.1.0]
	at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.invokeIfHaveRecords(KafkaMessageListenerContainer.java:1487) ~[spring-kafka-3.1.0.jar:3.1.0]
	at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.pollAndInvoke(KafkaMessageListenerContainer.java:1451) ~[spring-kafka-3.1.0.jar:3.1.0]
	at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.run(KafkaMessageListenerContainer.java:1322) ~[spring-kafka-3.1.0.jar:3.1.0]
	at java.base/java.util.concurrent.CompletableFuture$AsyncRun.run(CompletableFuture.java:1804) ~[na:na]
	at java.base/java.lang.Thread.run(Thread.java:1583) ~[na:na]
Caused by: org.springframework.kafka.support.serializer.DeserializationException: failed to deserialize
	at org.springframework.kafka.support.serializer.SerializationUtils.deserializationException(SerializationUtils.java:158) ~[spring-kafka-3.1.0.jar:3.1.0]
	at org.springframework.kafka.support.serializer.ErrorHandlingDeserializer.deserialize(ErrorHandlingDeserializer.java:218) ~[spring-kafka-3.1.0.jar:3.1.0]
	at org.apache.kafka.common.serialization.Deserializer.deserialize(Deserializer.java:73) ~[kafka-clients-3.6.0.jar:na]
	at org.apache.kafka.clients.consumer.internals.CompletedFetch.parseRecord(CompletedFetch.java:300) ~[kafka-clients-3.6.0.jar:na]
	at org.apache.kafka.clients.consumer.internals.CompletedFetch.fetchRecords(CompletedFetch.java:263) ~[kafka-clients-3.6.0.jar:na]
	at org.apache.kafka.clients.consumer.internals.AbstractFetch.fetchRecords(AbstractFetch.java:340) ~[kafka-clients-3.6.0.jar:na]
	at org.apache.kafka.clients.consumer.internals.AbstractFetch.collectFetch(AbstractFetch.java:306) ~[kafka-clients-3.6.0.jar:na]
	at org.apache.kafka.clients.consumer.KafkaConsumer.pollForFetches(KafkaConsumer.java:1262) ~[kafka-clients-3.6.0.jar:na]
	at org.apache.kafka.clients.consumer.KafkaConsumer.poll(KafkaConsumer.java:1186) ~[kafka-clients-3.6.0.jar:na]
	at org.apache.kafka.clients.consumer.KafkaConsumer.poll(KafkaConsumer.java:1159) ~[kafka-clients-3.6.0.jar:na]
	at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.pollConsumer(KafkaMessageListenerContainer.java:1658) ~[spring-kafka-3.1.0.jar:3.1.0]
	at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.doPoll(KafkaMessageListenerContainer.java:1633) ~[spring-kafka-3.1.0.jar:3.1.0]
	at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.pollAndInvoke(KafkaMessageListenerContainer.java:1430) ~[spring-kafka-3.1.0.jar:3.1.0]
	... 3 common frames omitted
Caused by: java.lang.IllegalStateException: No type information in headers and no default type provided
	at org.springframework.util.Assert.state(Assert.java:76) ~[spring-core-6.1.1.jar:6.1.1]
	at org.springframework.kafka.support.serializer.JsonDeserializer.deserialize(JsonDeserializer.java:583) ~[spring-kafka-3.1.0.jar:3.1.0]
	at org.springframework.kafka.support.serializer.ErrorHandlingDeserializer.deserialize(ErrorHandlingDeserializer.java:215) ~[spring-kafka-3.1.0.jar:3.1.0]
	... 14 common frames omitted

````

If you send good message

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
2025-03-06T16:58:42.937+05:30  INFO 664 --- [ntainer#0-0-C-1] c.a.w.e.h.ProductCreatedEventHandler     : Received a new event: ProductCreatedEvent(productId=c1b62df8-3c7e-46d8-9429-998102a72f10, title=Iphone 13, price=800, quantity=10)
````