# Kafka Transaction and database transaction

```
curl --location 'http://localhost:8080/transfers' \
--header 'Content-Type: application/json' \
--data '{
    "senderId": "100",
    "recepientId": "200",
    "amount": 120
}'
```

```
curl --location 'http://localhost:8080/transfers' \
--header 'Content-Type: application/json' \
--data '{
    "senderId": "101",
    "recepientId": "201",
    "amount": 200
}'
```

# Database

<img width="767" alt="Screenshot 2025-03-07 at 9 24 29 PM" src="https://github.com/user-attachments/assets/51d7b321-0d9a-45b1-a213-35cf996e4c10" />

# 

Withdrawl service

```java
[2m2025-03-07T21:19:59.757+05:30[0;39m [32m INFO[0;39m [35m43824[0;39m [2m---[0;39m [2m[ntainer#0-0-C-1][0;39m [2m[0;39m[36mo.s.k.l.KafkaMessageListenerContainer   [0;39m [2m:[0;39m amount-widthrawal-event: partitions assigned: [withdraw-money-topic-0, withdraw-money-topic-1, withdraw-money-topic-2]
[2m2025-03-07T21:20:41.324+05:30[0;39m [32m INFO[0;39m [35m43824[0;39m [2m---[0;39m [2m[ntainer#0-0-C-1][0;39m [2m[0;39m[36m.a.e.W.h.WithdrawalRequestedEventHandler[0;39m [2m:[0;39m Received a new withdrawal event: 120 
[2m2025-03-07T21:23:57.257+05:30[0;39m [32m INFO[0;39m [35m43824[0;39m [2m---[0;39m [2m[ntainer#0-0-C-1][0;39m [2m[0;39m[36m.a.e.W.h.WithdrawalRequestedEventHandler[0;39m [2m:[0;39m Received a new withdrawal event: 200 
```

Deposite Service

```java
[2m2025-03-07T21:20:26.265+05:30[0;39m [32m INFO[0;39m [35m43850[0;39m [2m---[0;39m [2m[ntainer#0-0-C-1][0;39m [2m[0;39m[36mo.s.k.l.KafkaMessageListenerContainer   [0;39m [2m:[0;39m amount-deposit-event: partitions assigned: [deposit-money-topic-0, deposit-money-topic-1, deposit-money-topic-2]
[2m2025-03-07T21:20:41.293+05:30[0;39m [32m INFO[0;39m [35m43850[0;39m [2m---[0;39m [2m[ntainer#0-0-C-1][0;39m [2m[0;39m[36mc.a.e.D.h.DepositRequestedEventHandler  [0;39m [2m:[0;39m Received a new deposit event: 120 
[2m2025-03-07T21:23:57.266+05:30[0;39m [32m INFO[0;39m [35m43850[0;39m [2m---[0;39m [2m[ntainer#0-0-C-1][0;39m [2m[0;39m[36mc.a.e.D.h.DepositRequestedEventHandler  [0;39m [2m:[0;39m Received a new deposit event: 200 
```

Even Kafka Topics will have all the data, haven't captured it.
