## Banking API
Simple banking API built with Spring Boot.
It provides basic functionality for managing customers, accounts, transactions and account statements.

## Tech Stack

Java 21
Spring Boot
Spring Data JPA
H2 (in-memory DB)
Maven


## Environment Variables

The application integrates with AWS SQS for publishing transaction events.

If you want to enable event publishing locally, you can set the following environment variables:

AWS_ACCESS_KEY_ID=
AWS_SECRET_ACCESS_KEY=
AWS_REGION=

Otherwise, the application can be run locally without AWS configuration for API validation


## How to run
mvn spring-boot:run
The application starts on port 8081 for customer microservice and 8082 for account microservice.
Base URL:
http://localhost:8081


## API Endpoints
Customers
Create customer
POST /api/v1/customers

Get customer by id
GET /api/v1/customers/{id}

Get all customers
GET /api/v1/customers

Update customer
PUT /api/v1/customers/{id}

Delete customer
DELETE /api/v1/customers/{id}


Accounts
Create account
POST /api/v1/accounts

Get account balance
GET /api/v1/accounts/{accountNumber}


Transactions
Register transaction (deposit / withdraw)
POST /api/v1/transactions


Reports
Get account statement by customer and date range
GET /api/v1/reports?customerId={id}&from={yyyy-MM-dd}&to={yyyy-MM-dd}


## Testing

Unit tests for service layer
Integration test for transaction registration and persistence

Run tests with:
mvn test


## Docker (optional)

The application can also be started using Docker. A docker-compose file is included in the repository.
