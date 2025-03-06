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

#Note - Retry Logic Testing 

- If all nodes are up, then message will be published and things are OK.
- Now I'm shutting down the kafka nodes and keeping only 1 up (since min insync replicaes = 2), expect it to retry
10 times

````
curl --location 'http://localhost:61086/products' \
--header 'Content-Type: application/json' \
--data '{
    "title":"Iphone 13",
    "price": 800,
    "quantity": 10
}'
````

`````
{
    "timestamp": "2025-03-06T06:53:31.436+00:00",
    "message": "org.springframework.kafka.core.KafkaProducerException: Failed to send",
    "details": "/products"
}
`````

Logs clearly shows retried for 10 times and exhausted 

````
2025-03-06T12:23:21.041+05:30  INFO 35019 --- [o-auto-1-exec-2] o.a.k.clients.producer.KafkaProducer     : [Producer clientId=producer-1] Instantiated an idempotent producer.
2025-03-06T12:23:21.050+05:30  INFO 35019 --- [o-auto-1-exec-2] o.a.kafka.common.utils.AppInfoParser     : Kafka version: 3.6.0
2025-03-06T12:23:21.050+05:30  INFO 35019 --- [o-auto-1-exec-2] o.a.kafka.common.utils.AppInfoParser     : Kafka commitId: 60e845626d8a465a
2025-03-06T12:23:21.050+05:30  INFO 35019 --- [o-auto-1-exec-2] o.a.kafka.common.utils.AppInfoParser     : Kafka startTimeMs: 1741244001050
2025-03-06T12:23:21.060+05:30  INFO 35019 --- [ad | producer-1] org.apache.kafka.clients.Metadata        : [Producer clientId=producer-1] Cluster ID: WnLkTHhkQaiJbwP8FClPhw
2025-03-06T12:23:21.061+05:30  INFO 35019 --- [ad | producer-1] o.a.k.c.p.internals.TransactionManager   : [Producer clientId=producer-1] ProducerId set to 7 with epoch 0
2025-03-06T12:23:21.096+05:30  WARN 35019 --- [ad | producer-1] o.a.k.clients.producer.internals.Sender  : [Producer clientId=producer-1] Got error produce response with correlation id 4 on topic-partition topic2-2, retrying (9 attempts left). Error: NOT_ENOUGH_REPLICAS
2025-03-06T12:23:22.104+05:30  WARN 35019 --- [ad | producer-1] o.a.k.clients.producer.internals.Sender  : [Producer clientId=producer-1] Got error produce response with correlation id 5 on topic-partition topic2-2, retrying (8 attempts left). Error: NOT_ENOUGH_REPLICAS
2025-03-06T12:23:23.249+05:30  WARN 35019 --- [ad | producer-1] o.a.k.clients.producer.internals.Sender  : [Producer clientId=producer-1] Got error produce response with correlation id 6 on topic-partition topic2-2, retrying (7 attempts left). Error: NOT_ENOUGH_REPLICAS
2025-03-06T12:23:24.256+05:30  WARN 35019 --- [ad | producer-1] o.a.k.clients.producer.internals.Sender  : [Producer clientId=producer-1] Got error produce response with correlation id 7 on topic-partition topic2-2, retrying (6 attempts left). Error: NOT_ENOUGH_REPLICAS
2025-03-06T12:23:25.280+05:30  WARN 35019 --- [ad | producer-1] o.a.k.clients.producer.internals.Sender  : [Producer clientId=producer-1] Got error produce response with correlation id 8 on topic-partition topic2-2, retrying (5 attempts left). Error: NOT_ENOUGH_REPLICAS
2025-03-06T12:23:26.301+05:30  WARN 35019 --- [ad | producer-1] o.a.k.clients.producer.internals.Sender  : [Producer clientId=producer-1] Got error produce response with correlation id 9 on topic-partition topic2-2, retrying (4 attempts left). Error: NOT_ENOUGH_REPLICAS
2025-03-06T12:23:27.345+05:30  WARN 35019 --- [ad | producer-1] o.a.k.clients.producer.internals.Sender  : [Producer clientId=producer-1] Got error produce response with correlation id 10 on topic-partition topic2-2, retrying (3 attempts left). Error: NOT_ENOUGH_REPLICAS
2025-03-06T12:23:28.367+05:30  WARN 35019 --- [ad | producer-1] o.a.k.clients.producer.internals.Sender  : [Producer clientId=producer-1] Got error produce response with correlation id 11 on topic-partition topic2-2, retrying (2 attempts left). Error: NOT_ENOUGH_REPLICAS
2025-03-06T12:23:29.391+05:30  WARN 35019 --- [ad | producer-1] o.a.k.clients.producer.internals.Sender  : [Producer clientId=producer-1] Got error produce response with correlation id 12 on topic-partition topic2-2, retrying (1 attempts left). Error: NOT_ENOUGH_REPLICAS
2025-03-06T12:23:30.416+05:30  WARN 35019 --- [ad | producer-1] o.a.k.clients.producer.internals.Sender  : [Producer clientId=producer-1] Got error produce response with correlation id 13 on topic-partition topic2-2, retrying (0 attempts left). Error: NOT_ENOUGH_REPLICAS
2025-03-06T12:23:31.431+05:30 ERROR 35019 --- [ad | producer-1] o.s.k.support.LoggingProducerListener    : Exception thrown when sending a message with key='e1dddc45-904a-4b16-bf33-ca328fc5107a' and payload='com.appsdeveloperblog.ws.products.service.ProductCreatedEvent@2b2d017b' to topic topic2:

org.apache.kafka.common.errors.NotEnoughReplicasException: Messages are rejected since there are fewer in-sync replicas than required.

2025-03-06T12:23:31.431+05:30 ERROR 35019 --- [o-auto-1-exec-2] c.a.ws.products.rest.ProductController   : org.springframework.kafka.core.KafkaProducerException: Failed to send

java.util.concurrent.ExecutionException: org.springframework.kafka.core.KafkaProducerException: Failed to send
	at java.base/java.util.concurrent.CompletableFuture.reportGet(CompletableFuture.java:396) ~[na:na]
	at java.base/java.util.concurrent.CompletableFuture.get(CompletableFuture.java:2073) ~[na:na]
	at com.appsdeveloperblog.ws.products.service.ProductServiceImpl.createProduct(ProductServiceImpl.java:38) ~[classes/:na]
	at com.appsdeveloperblog.ws.products.rest.ProductController.createProduct(ProductController.java:32) ~[classes/:na]
	at java.base/jdk.internal.reflect.DirectMethodHandleAccessor.invoke(DirectMethodHandleAccessor.java:103) ~[na:na]
	at java.base/java.lang.reflect.Method.invoke(Method.java:580) ~[na:na]
	at org.springframework.web.method.support.InvocableHandlerMethod.doInvoke(InvocableHandlerMethod.java:254) ~[spring-web-6.1.1.jar:6.1.1]
	at org.springframework.web.method.support.InvocableHandlerMethod.invokeForRequest(InvocableHandlerMethod.java:182) ~[spring-web-6.1.1.jar:6.1.1]
	at org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod.invokeAndHandle(ServletInvocableHandlerMethod.java:118) ~[spring-webmvc-6.1.1.jar:6.1.1]
	at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.invokeHandlerMethod(RequestMappingHandlerAdapter.java:917) ~[spring-webmvc-6.1.1.jar:6.1.1]
	at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.handleInternal(RequestMappingHandlerAdapter.java:829) ~[spring-webmvc-6.1.1.jar:6.1.1]
	at org.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter.handle(AbstractHandlerMethodAdapter.java:87) ~[spring-webmvc-6.1.1.jar:6.1.1]
	at org.springframework.web.servlet.DispatcherServlet.doDispatch(DispatcherServlet.java:1089) ~[spring-webmvc-6.1.1.jar:6.1.1]
	at org.springframework.web.servlet.DispatcherServlet.doService(DispatcherServlet.java:979) ~[spring-webmvc-6.1.1.jar:6.1.1]
	at org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:1014) ~[spring-webmvc-6.1.1.jar:6.1.1]
	at org.springframework.web.servlet.FrameworkServlet.doPost(FrameworkServlet.java:914) ~[spring-webmvc-6.1.1.jar:6.1.1]
	at jakarta.servlet.http.HttpServlet.service(HttpServlet.java:590) ~[tomcat-embed-core-10.1.16.jar:6.0]
	at org.springframework.web.servlet.FrameworkServlet.service(FrameworkServlet.java:885) ~[spring-webmvc-6.1.1.jar:6.1.1]
	at jakarta.servlet.http.HttpServlet.service(HttpServlet.java:658) ~[tomcat-embed-core-10.1.16.jar:6.0]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:205) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:149) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
	at org.apache.tomcat.websocket.server.WsFilter.doFilter(WsFilter.java:51) ~[tomcat-embed-websocket-10.1.16.jar:10.1.16]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:174) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:149) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
	at org.springframework.web.filter.RequestContextFilter.doFilterInternal(RequestContextFilter.java:100) ~[spring-web-6.1.1.jar:6.1.1]
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:116) ~[spring-web-6.1.1.jar:6.1.1]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:174) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:149) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
	at org.springframework.web.filter.FormContentFilter.doFilterInternal(FormContentFilter.java:93) ~[spring-web-6.1.1.jar:6.1.1]
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:116) ~[spring-web-6.1.1.jar:6.1.1]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:174) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:149) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
	at org.springframework.web.filter.CharacterEncodingFilter.doFilterInternal(CharacterEncodingFilter.java:201) ~[spring-web-6.1.1.jar:6.1.1]
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:116) ~[spring-web-6.1.1.jar:6.1.1]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:174) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:149) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
	at org.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:167) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
	at org.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:90) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
	at org.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:482) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
	at org.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:115) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
	at org.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:93) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
	at org.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:74) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
	at org.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:340) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
	at org.apache.coyote.http11.Http11Processor.service(Http11Processor.java:391) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
	at org.apache.coyote.AbstractProcessorLight.process(AbstractProcessorLight.java:63) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
	at org.apache.coyote.AbstractProtocol$ConnectionHandler.process(AbstractProtocol.java:896) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
	at org.apache.tomcat.util.net.NioEndpoint$SocketProcessor.doRun(NioEndpoint.java:1744) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
	at org.apache.tomcat.util.net.SocketProcessorBase.run(SocketProcessorBase.java:52) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
	at org.apache.tomcat.util.threads.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1191) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
	at org.apache.tomcat.util.threads.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:659) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
	at org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
	at java.base/java.lang.Thread.run(Thread.java:1583) ~[na:na]
Caused by: org.springframework.kafka.core.KafkaProducerException: Failed to send
	at org.springframework.kafka.core.KafkaTemplate.lambda$buildCallback$9(KafkaTemplate.java:842) ~[spring-kafka-3.1.0.jar:3.1.0]
	at org.springframework.kafka.core.DefaultKafkaProducerFactory$CloseSafeProducer$1.onCompletion(DefaultKafkaProducerFactory.java:1058) ~[spring-kafka-3.1.0.jar:3.1.0]
	at org.apache.kafka.clients.producer.KafkaProducer$AppendCallbacks.onCompletion(KafkaProducer.java:1490) ~[kafka-clients-3.6.0.jar:na]
	at org.apache.kafka.clients.producer.internals.ProducerBatch.completeFutureAndFireCallbacks(ProducerBatch.java:273) ~[kafka-clients-3.6.0.jar:na]
	at org.apache.kafka.clients.producer.internals.ProducerBatch.done(ProducerBatch.java:234) ~[kafka-clients-3.6.0.jar:na]
	at org.apache.kafka.clients.producer.internals.ProducerBatch.completeExceptionally(ProducerBatch.java:198) ~[kafka-clients-3.6.0.jar:na]
	at org.apache.kafka.clients.producer.internals.Sender.failBatch(Sender.java:798) ~[kafka-clients-3.6.0.jar:na]
	at org.apache.kafka.clients.producer.internals.Sender.failBatch(Sender.java:787) ~[kafka-clients-3.6.0.jar:na]
	at org.apache.kafka.clients.producer.internals.Sender.failBatch(Sender.java:739) ~[kafka-clients-3.6.0.jar:na]
	at org.apache.kafka.clients.producer.internals.Sender.completeBatch(Sender.java:678) ~[kafka-clients-3.6.0.jar:na]
	at org.apache.kafka.clients.producer.internals.Sender.lambda$null$1(Sender.java:619) ~[kafka-clients-3.6.0.jar:na]
	at java.base/java.util.ArrayList.forEach(ArrayList.java:1596) ~[na:na]
	at org.apache.kafka.clients.producer.internals.Sender.lambda$handleProduceResponse$2(Sender.java:606) ~[kafka-clients-3.6.0.jar:na]
	at java.base/java.lang.Iterable.forEach(Iterable.java:75) ~[na:na]
	at org.apache.kafka.clients.producer.internals.Sender.handleProduceResponse(Sender.java:606) ~[kafka-clients-3.6.0.jar:na]
	at org.apache.kafka.clients.producer.internals.Sender.lambda$sendProduceRequest$5(Sender.java:885) ~[kafka-clients-3.6.0.jar:na]
	at org.apache.kafka.clients.ClientResponse.onComplete(ClientResponse.java:154) ~[kafka-clients-3.6.0.jar:na]
	at org.apache.kafka.clients.NetworkClient.completeResponses(NetworkClient.java:594) ~[kafka-clients-3.6.0.jar:na]
	at org.apache.kafka.clients.NetworkClient.poll(NetworkClient.java:586) ~[kafka-clients-3.6.0.jar:na]
	at org.apache.kafka.clients.producer.internals.Sender.runOnce(Sender.java:344) ~[kafka-clients-3.6.0.jar:na]
	at org.apache.kafka.clients.producer.internals.Sender.run(Sender.java:247) ~[kafka-clients-3.6.0.jar:na]
	... 1 common frames omitted
Caused by: org.apache.kafka.common.errors.NotEnoughReplicasException: Messages are rejected since there are fewer in-sync replicas than required.
````