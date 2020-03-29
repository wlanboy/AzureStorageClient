pipeline {
  agent any
  tools { 
     jdk 'java11slave' 
  }
  options {
    buildDiscarder(logRotator(numToKeepStr: '1'))
  }
  parameters {
      booleanParam(defaultValue: false, description: 'Publish to DockerHub', name: 'PUBLISHIMAGE')
  }
  environment {
    LOGSTASH = 'nuc:5044'
  }  
  stages {
    stage('Git') {
      steps {
        git(url: 'https://github.com/wlanboy/AzureStorageClient.git', branch: 'master')
      }
    }
    stage('Build') {
      steps {
        sh 'mvn clean package -DskipTests'
      }
    }    
    stage('Container') {
      steps {
        sh 'docker build -t azurestorageclient:latest . --build-arg JAR_FILE=./target/azurestorageclient-0.0.1-SNAPSHOT.jar'
      }
    }
    stage('Publish') {
      when { expression { params.PUBLISHIMAGE == true } }
      steps {
        withDockerRegistry([ credentialsId: "dockerhub", url: "" ]) {
          sh 'docker push wlanboy/azurestorageclient:latest'
        }
      }
    }
  }
}
