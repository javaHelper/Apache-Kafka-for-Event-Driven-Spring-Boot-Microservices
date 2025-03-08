kafka-topics --bootstrap-server localhost:9092 --create --topic orders-events --partitions 3 --replication-factor 3

kafka-topics --bootstrap-server localhost:9092 --create --topic products-commands --partitions 3 --replication-factor 3

kafka-topics --bootstrap-server localhost:9092 --create --topic products-events --partitions 3 --replication-factor 3

kafka-topics --bootstrap-server localhost:9092 --create --topic payments-commands --partitions 3 --replication-factor 3

kafka-topics --bootstrap-server localhost:9092 --create --topic payments-events --partitions 3 --replication-factor 3

kafka-topics --bootstrap-server localhost:9092 --create --topic orders-commands --partitions 3 --replication-factor 3

