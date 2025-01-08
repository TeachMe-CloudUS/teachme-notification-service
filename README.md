# teachme-notification-service

This repository contains the TeachMe Notification Service API, a microservice for managing notifications within the TeachMe platform.

## Development Setup

### Prerequisites

Ensure you have the following installed:

- **Docker**

As this repository depends on two java packages that are uploaded on github an adaptation in the `.m2/settings.xml` is needed.
Ensure to have the following setting set:

> If the `~/.m2/settings.xml` file does not exist, just create it!

```xml
<settings>
    <servers>
        <server>
            <id>github</id>
            <username>YOUR_GITHUB_USERNAME</username>
            <password>YOUR_GITHUB_TOKEN_PWD</password>
        </server>
    </servers>
</settings>
```

### Starting the Development Environment

The project includes Dockerfile.dev and docker-compose.dev.yml files for setting up the development environment.

1. Clone the repository
2. Start the development container with Docker Compose:

```bash
docker compose -f docker-compose.standalone.yml up --build
```

This command will build and start the service along with all dependencies that are needed for local development, which are:
- MongoDB
- Kafka Broker (with Zookeeper)
- Azure Function (local)

The application should then by default run on http://localhost:8080.

As the API requires a [JWT](https://jwt.io/) for the testing purposes we provide one here:

```text
eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJPbmxpbmUgSldUIEJ1aWxkZXIiLCJpYXQiOjE3MzYyNTA5MTMsImV4cCI6MTc2Nzc4NjkxMywiYXVkIjoid3d3LmV4YW1wbGUuY29tIiwic3ViIjoidGVzdC11c2VyaWQifQ.cWppiqMfnU-ilGSPK5a8vAsIPr4f_cTG16F9ZPWjMfc
```

Just copy it and use it in your requests to the backend in the `Authorization`-Header.
The following shows an example using curl:

```bash
curl -X GET http://localhost:8080/api/v1/notifications -H "Authorization: Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJPbmxpbmUgSldUIEJ1aWxkZXIiLCJpYXQiOjE3MzYyNTA5MTMsImV4cCI6MTc2Nzc4NjkxMywiYXVkIjoid3d3LmV4YW1wbGUuY29tIiwic3ViIjoidGVzdC11c2VyaWQifQ.cWppiqMfnU-ilGSPK5a8vAsIPr4f_cTG16F9ZPWjMfc"
```

This repository is also part of the [Dev Box Project](https://github.com/TeachMe-CloudUS/teachme-dev-box) which is the local setup to start the whole application including all other microservices and the frontend locally.