#!/bin/bash
REGISTRY_PATH="registry.git.cf.ac.uk/dcom_cih"
docker login $REGISTRY_PATH	
mvn clean
mvn package
docker build -t $REGISTRY_PATH/buldingcodeandrules . --platform=linux/amd64 
docker push $REGISTRY_PATH/buldingcodeandrules