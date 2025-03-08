# saga-pattern-spring-boot-demo

Demonstration of SAGA Orchestration Design Pattern using Spring Boot and Kafka

# Happy Path

```
curl --location 'http://localhost:8081/products' \
--header 'Content-Type: application/json' \
--data '{
    "name": "iphone 11",
    "price": 1500,
    "quantity": 5
}'
```

```
{
    "id": "6ac1b200-7c5d-40ef-b503-25141073194b",
    "name": "iphone 11",
    "price": 1500,
    "quantity": 5
}
```

```
curl --location 'http://localhost:8080/orders' \
--header 'Content-Type: application/json' \
--data '{
    "productId": "6ac1b200-7c5d-40ef-b503-25141073194b",
    "productQuantity": 1,
    "customerId": "3751f8f8-9a19-476e-80d7-b1be72babf21"
}'
```

```
{
    "orderId": "7a6df7b5-2800-4151-bd5b-d4f4dc2b0c6e",
    "customerId": "3751f8f8-9a19-476e-80d7-b1be72babf21",
    "productId": "6ac1b200-7c5d-40ef-b503-25141073194b",
    "productQuantity": 1,
    "status": "CREATED"
}
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

# Now check compensating txn, down the credit card processor service

```sh
curl --location 'http://localhost:8080/orders' \
--header 'Content-Type: application/json' \
--data '{
    "productId": "6ac1b200-7c5d-40ef-b503-25141073194b",
    "productQuantity": 1,
    "customerId": "3751f8f8-9a19-476e-80d7-b1be72babf21"
}'
```

```json
{
    "orderId": "46f9749b-d5f5-4b2b-810a-cd6cb778503c",
    "customerId": "3751f8f8-9a19-476e-80d7-b1be72babf21",
    "productId": "6ac1b200-7c5d-40ef-b503-25141073194b",
    "productQuantity": 1,
    "status": "CREATED"
}
```

```
curl --location 'http://localhost:8080/orders/46f9749b-d5f5-4b2b-810a-cd6cb778503c/history'
```

```json
[
    {
        "id": "593f5c49-1413-499d-8cea-1c9059b2829f",
        "orderId": "46f9749b-d5f5-4b2b-810a-cd6cb778503c",
        "status": "CREATED",
        "createdAt": "2025-03-08T17:41:27.822+00:00"
    },
    {
        "id": "793723f8-6b16-459c-a9fa-ef0c301d0201",
        "orderId": "46f9749b-d5f5-4b2b-810a-cd6cb778503c",
        "status": "REJECTED",
        "createdAt": "2025-03-08T17:41:43.691+00:00"
    }
]
```
