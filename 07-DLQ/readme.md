#

````
prateekashtikar@Prateeks-MBP Prateek % kafka-console-producer --bootstrap-server localhost:9092 --topic products-created-events-topic --property "parse.key=true" --property "key.separator=:"
>1:{jkdsjkdsjkds}
>
````

- DLT is by default base64 decoding 

````
kafka-console-consumer --bootstrap-server localhost:9092 --topic products-created-events-topic.DLT --property print.key=true --property print.value=true --from-beginning 
1	"e2prZHNqa2Rzamtkc30="

````

<img width="1197" alt="Screenshot 2025-03-06 at 5 28 18 PM" src="https://github.com/user-attachments/assets/3ee98870-420f-4646-8ebf-f12f6c3a0171" />
