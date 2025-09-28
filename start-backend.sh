#!/bin/bash

# Enable debugging
set -x


echo "Navigating to the backend directory..."
cd "/home/simon/projects/devops/kruso hemsida/product-hemsida/product-hemsida/backend/backend-java" || { echo "Failed to navigate to backend directory"; exit 1; }

docker-compose up -d

./mvnw spring-boot:run
