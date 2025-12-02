# Spring Boot Demo Application

This is a demo Spring Boot application showcasing an event-driven architecture using the Transactional Outbox Pattern with Kafka and MySQL.

## Tech Stack

- **Java 21**
- **Spring Boot 3.4.0**
- **Spring Data JPA** (MySQL)
- **Spring Kafka**
- **Docker & Docker Compose**
- **Kubernetes** (Manifests included)

## Prerequisites

- JDK 21
- Docker Desktop

## How to Run Locally

1.  **Start Infrastructure**:
    Run the following command to start MySQL, Zookeeper, and Kafka:
    ```bash
    docker-compose up -d
    ```

2.  **Build and Run Application**:
    ```bash
    ./mvnw spring-boot:run
    ```

## Project Structure

- `src/main/java`: Source code
    - `scheduler`: Contains `OutboxScheduler` for the Outbox pattern.
    - `repository`: JPA repositories.
- `k8s/`: Kubernetes manifests for deployment.
- `docker-compose.yml`: Local development infrastructure.

## How to Deploy to Kubernetes

1.  **Configure Secrets**:
    Copy `k8s/mysql-secret.example.yaml` to `k8s/mysql-secret.yaml` and update the values if needed.
    ```bash
    kubectl apply -f k8s/mysql-secret.yaml
    ```

2.  **Deploy to Kubernetes**:
    ```bash
    kubectl apply -f k8s/
    ```
