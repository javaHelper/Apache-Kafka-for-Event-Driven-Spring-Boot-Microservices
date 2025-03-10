# saga-pattern-spring-boot-demo

Demonstration of SAGA Orchestration Design Pattern using Spring Boot and Kafka

```sh
curl --location 'http://localhost:8081/products' \
--header 'Content-Type: application/json' \
--data '{
    "name": "iphone 11",
    "price": 1500,
    "quantity": 5
}'
```

```shell
{
    "id": "92049e90-0c8b-4b5c-8042-737eab04002f",
    "name": "iphone 11",
    "price": 1500,
    "quantity": 5
}
```


```shell
curl --location 'http://localhost:8080/orders' \
--header 'Content-Type: application/json' \
--data '{
    "productId": "92049e90-0c8b-4b5c-8042-737eab04002f",
    "productQuantity": 1,
    "customerId": "3751f8f8-9a19-476e-80d7-b1be72babf21"
}'
```

```
{
    "orderId": "af8b67cb-769b-4148-99bf-e67b22f8a784",
    "customerId": "3751f8f8-9a19-476e-80d7-b1be72babf21",
    "productId": "92049e90-0c8b-4b5c-8042-737eab04002f",
    "productQuantity": 1,
    "status": "CREATED"
}
```

```
kafka-console-consumer --bootstrap-server localhost:9092 --topic orders-events --from-beginning
{"orderId":"af8b67cb-769b-4148-99bf-e67b22f8a784","customerId":"3751f8f8-9a19-476e-80d7-b1be72babf21","productId":"92049e90-0c8b-4b5c-8042-737eab04002f","productQuantity":1}
{"orderId":"af8b67cb-769b-4148-99bf-e67b22f8a784"}
```

```
kafka-console-consumer --bootstrap-server localhost:9092 --topic products-commands --from-beginning
{"productId":"92049e90-0c8b-4b5c-8042-737eab04002f","productQuantity":1,"orderId":"af8b67cb-769b-4148-99bf-e67b22f8a784"}
```

```
afka-console-consumer --bootstrap-server localhost:9092 --topic products-events --from-beginning
{"orderId":"af8b67cb-769b-4148-99bf-e67b22f8a784","productId":"92049e90-0c8b-4b5c-8042-737eab04002f","productPrice":1500.00,"productQuantity":1}
```

```
kafka-console-consumer --bootstrap-server localhost:9092 --topic payments-commands --from-beginning
{"orderId":"af8b67cb-769b-4148-99bf-e67b22f8a784","productId":"92049e90-0c8b-4b5c-8042-737eab04002f","productPrice":1500.00,"productQuantity":1}
```

```
kafka-console-consumer --bootstrap-server localhost:9092 --topic payments-events --from-beginning
{"orderId":"af8b67cb-769b-4148-99bf-e67b22f8a784","paymentId":"5e660c5d-a67a-41f7-afc9-a082673b9686"}
```

```
kafka-console-consumer --bootstrap-server localhost:9092 --topic orders-commands --from-beginning
{"orderId":"af8b67cb-769b-4148-99bf-e67b22f8a784"}
```

```
curl --location 'http://localhost:8080/orders/be31fac8-8b9b-4cef-af01-bbed00bc499a/history'
```

```json
[
    {
        "id": "3072b4fe-6529-4ec3-8b97-ca5f310ab995",
        "orderId": "be31fac8-8b9b-4cef-af01-bbed00bc499a",
        "status": "CREATED",
        "createdAt": "2025-03-08T17:01:35.813+00:00"
    },
    {
        "id": "6bdfe9df-8d3d-4306-ae8d-adbe4b078110",
        "orderId": "be31fac8-8b9b-4cef-af01-bbed00bc499a",
        "status": "APPROVED",
        "createdAt": "2025-03-08T17:01:37.098+00:00"
    }
]
```
