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

# Retry and timeout

when all brokers are up, then things works well - tested
when only 1 broker is up, then should expect

````
2025-03-06T12:52:47.634+05:30  WARN 41193 --- [ad | producer-1] o.a.k.clients.producer.internals.Sender  : [Producer clientId=producer-1] Got error produce response with correlation id 1038 on topic-partition topic2-0, retrying (2147482612 attempts left). Error: NOT_ENOUGH_REPLICAS
2025-03-06T12:52:47.684+05:30 ERROR 41193 --- [ad | producer-1] o.s.k.support.LoggingProducerListener    : Exception thrown when sending a message with key='04104e1e-dc93-479e-bf86-c291f4f8faca' and payload='com.appsdeveloperblog.ws.products.service.ProductCreatedEvent@26f8cb6f' to topic topic2:

org.apache.kafka.common.errors.TimeoutException: Expiring 1 record(s) for topic2-0:120002 ms has passed since batch creation

2025-03-06T12:52:47.683+05:30 ERROR 41193 --- [o-auto-1-exec-1] c.a.ws.products.rest.ProductController   : org.springframework.kafka.core.KafkaProducerException: Failed to send

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
	at org.apache.kafka.clients.producer.internals.Sender.sendProducerData(Sender.java:422) ~[kafka-clients-3.6.0.jar:na]
	at org.apache.kafka.clients.producer.internals.Sender.runOnce(Sender.java:343) ~[kafka-clients-3.6.0.jar:na]
	at org.apache.kafka.clients.producer.internals.Sender.run(Sender.java:247) ~[kafka-clients-3.6.0.jar:na]
	... 1 common frames omitted
Caused by: org.apache.kafka.common.errors.TimeoutException: Expiring 1 record(s) for topic2-0:120002 ms has passed since batch creation
````