<img width="1199" alt="Screenshot 2025-03-07 at 5 49 39 PM" src="https://github.com/user-attachments/assets/99171378-afee-4236-9708-99bc975dc884" /># Kafka Transaction

```shell
curl --location 'http://localhost:58167/transfers' \
--header 'Content-Type: application/json' \
--data '{
    "senderId": "100",
    "recepientId": "200",
    "amount": 120
}'
```

Response:

```
true
```

Deposite Service Logs

```
[2m2025-03-07T17:45:09.106+05:30[0;39m [32m INFO[0;39m [35m26326[0;39m [2m---[0;39m [2m[ntainer#0-0-C-1][0;39m [2m[0;39m[36mo.a.k.c.c.internals.ConsumerCoordinator [0;39m [2m:[0;39m [Consumer clientId=consumer-amount-deposit-event-1, groupId=amount-deposit-event] Setting offset for partition deposit-money-topic-0 to the committed offset FetchPosition{offset=0, offsetEpoch=Optional.empty, currentLeader=LeaderAndEpoch{leader=Optional[localhost:9096 (id: 3 rack: null)], epoch=0}}
[2m2025-03-07T17:45:09.111+05:30[0;39m [32m INFO[0;39m [35m26326[0;39m [2m---[0;39m [2m[ntainer#0-0-C-1][0;39m [2m[0;39m[36mo.s.k.l.KafkaMessageListenerContainer   [0;39m [2m:[0;39m amount-deposit-event: partitions assigned: [deposit-money-topic-0, deposit-money-topic-1, deposit-money-topic-2]
[2m2025-03-07T17:45:14.750+05:30[0;39m [32m INFO[0;39m [35m26326[0;39m [2m---[0;39m [2m[ntainer#0-0-C-1][0;39m [2m[0;39m[36mc.a.e.D.h.DepositRequestedEventHandler  [0;39m [2m:[0;39m Received a new deposit event: 120 
```

<img width="1199" alt="Screenshot 2025-03-07 at 5 50 24 PM" src="https://github.com/user-attachments/assets/70970cfc-940a-4180-8103-52b067009135" />

-----------

<img width="1199" alt="Screenshot 2025-03-07 at 5 49 39 PM" src="https://github.com/user-attachments/assets/1c7a04d0-f85c-4228-952a-f5ad1050d749" />

- Down the mockservice

````
25-03-07T17:56:29.944+05:30[0;39m [31mERROR[0;39m [35m26328[0;39m [2m---[0;39m [2m[nio-8080-exec-4][0;39m [2m[0;39m[36mc.a.e.t.service.TransferServiceImpl     [0;39m [2m:[0;39m I/O error on GET request for "http://localhost:8082/response/200": Connection refused

org.springframework.web.client.ResourceAccessException: I/O error on GET request for "http://localhost:8082/response/200": Connection refused
	at org.springframework.web.client.RestTemplate.createResourceAccessException(RestTemplate.java:915) ~[spring-web-6.1.4.jar:6.1.4]
	at org.springframework.web.client.RestTemplate.doExecute(RestTemplate.java:895) ~[spring-web-6.1.4.jar:6.1.4]
	at org.springframework.web.client.RestTemplate.execute(RestTemplate.java:790) ~[spring-web-6.1.4.jar:6.1.4]
	at org.springframework.web.client.RestTemplate.exchange(RestTemplate.java:672) ~[spring-web-6.1.4.jar:6.1.4]
	at com.appsdeveloperblog.estore.transfers.service.TransferServiceImpl.callRemoteServce(TransferServiceImpl.java:63) ~[classes/:na]
	at com.appsdeveloperblog.estore.transfers.service.TransferServiceImpl.transfer(TransferServiceImpl.java:48) ~[classes/:na]
	at java.base/jdk.internal.reflect.DirectMethodHandleAccessor.invoke(DirectMethodHandleAccessor.java:103) ~[na:na]

[2m2025-03-07T17:56:29.961+05:30[0;39m [32mTRACE[0;39m [35m26328[0;39m [2m---[0;39m [2m[nio-8080-exec-4][0;39m [2m[0;39m[36mo.s.t.i.TransactionInterceptor          [0;39m [2m:[0;39m Completing transaction for [com.appsdeveloperblog.estore.transfers.service.TransferServiceImpl.transfer] after exception: com.appsdeveloperblog.estore.transfers.error.TransferServiceException: org.springframework.web.client.ResourceAccessException: I/O error on GET request for "http://localhost:8082/response/200": Connection refused
[2m2025-03-07T17:56:29.964+05:30[0;39m [32mDEBUG[0;39m [35m26328[0;39m [2m---[0;39m [2m[nio-8080-exec-4][0;39m [2m[0;39m[36mo.s.k.t.KafkaTransactionManager         [0;39m [2m:[0;39m Initiating transaction rollback
[2m2025-03-07T17:56:29.968+05:30[0;39m [32m INFO[0;39m [35m26328[0;39m [2m---[0;39m [2m[nio-8080-exec-4][0;39m [2m[0;39m[36mo.a.k.clients.producer.KafkaProducer    [0;39m [2m:[0;39m [Producer clientId=producer-transfer-service-6e459418bd95106be5839a0f938f9003-0, transactionalId=transfer-service-6e459418bd95106be5839a0f938f9003-0] Aborting incomplete transaction
[2m2025-03-07T17:56:29.996+05:30[0;39m [31mERROR[0;39m [35m26328[0;39m [2m---[0;39m [2m[nio-8080-exec-4][0;39m [2m[0;39m[36mo.a.c.c.C.[.[.[/].[dispatcherServlet]   [0;39m [2m:[0;39m Servlet.service() for servlet [dispatcherServlet] in context with path [] threw exception [Request processing failed: com.appsdeveloperblog.estore.transfers.error.TransferServiceException: org.springframework.web.client.ResourceAccessException: I/O error on GET request for "http://localhost:8082/response/200": Connection refused] with root cause

java.net.ConnectException: Connection refused
	at java.base/sun.nio.ch.Net.connect0(Native Method) ~[na:na]
	at java.base/sun.nio.ch.Net.connect(Net.java:589) ~[na:na]
	at java.base/sun.nio.ch.Net.connect(Net.java:578) ~[na:na]
	at java.base/sun.nio.ch.NioSocketImpl.connect(NioSocketImpl.java:583) ~[na:na]
	at java.base/java.net.Socket.connect(Socket.java:751) ~[na:na]
	at java.base/java.net.Socket.connect(Socket.java:686) ~[na:na]
	
````


