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