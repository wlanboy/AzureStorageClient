# Azure
Spring Boot Azure Storage Client (MVC and REST)

## Dependencies
At least: Java 8 and Maven 3.5

## Build Simple Storage Client 
mvn package -DskipTests=true

## Run Simple Storage Client 
### Environment variables

### Windows
java -jar target\azurestorageclient.jar

### Linux (service enabled)
./target/azurestorageclient.jar start

## Docker build
docker build -t azurestorageclient:latest . --build-arg JAR_FILE=./target/azurestorageclient.jar

## Docker run
docker run --name azurestorageclient -d -p 8001:8001 -v /tmp:/tmp azurestorageclient:latest

## Usage
http://127.0.0.1:8001/ for Storage Client
http://127.0.0.1:8001/message for Queue Client