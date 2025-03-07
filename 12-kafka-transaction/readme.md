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

