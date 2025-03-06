#

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
5d9d5876-d9d9-46aa-8f6d-6f329a7684a2
````

````
kafka-console-consumer --bootstrap-server localhost:9092 --topic products-created-events-topic.DLT --property print.key=true --property print.value=true --from-beginning 
5d9d5876-d9d9-46aa-8f6d-6f329a7684a2	{"productId":"5d9d5876-d9d9-46aa-8f6d-6f329a7684a2","title":"Iphone 13","price":800,"quantity":10}
````