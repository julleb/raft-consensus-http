#raft-consensus-http

## Run
mvn spring-boot:run
mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=1337 --raft.cluster.nodeName=http://localhost:1337"
mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=1337 --raft.cluster.nodeName=http://localhost:1338"