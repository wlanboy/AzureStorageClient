# Azure
Spring Boot Azure Storage Client (MVC and REST)

## Dependencies
At least: Java 8 and Maven 3.5

## Build Simple Storage Client 
mvn package -DskipTests=true

## Run Simple Storage Client 
### Environment variables

### Windows
java -jar target\AzureStorageClient.jar

### Linux (service enabled)
./target/AzureStorageClient.jar start

## Docker build
docker build -t azureStorageClient:latest . --build-arg JAR_FILE=./target/AzureStorageClient.jar

## Docker run
docker run --name azureStorageClient -d -p 8001:8001 -v /tmp:/tmp azureStorageClient:latest
