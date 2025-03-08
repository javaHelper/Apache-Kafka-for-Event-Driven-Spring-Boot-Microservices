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
