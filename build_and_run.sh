#!/bin/bash

# Build the project
./gradlew build -x test

if [ $? -ne 0 ]; then
    echo "Gradle build failed. Exiting."
    exit 1
fi

docker-compose build

if [ $? -ne 0 ]; then
    echo "Docker build failed. Exiting."
    exit 1
fi

docker-compose up
