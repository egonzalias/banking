Banking API
Simple banking API built with Spring Boot.
It provides basic functionality for managing customers, accounts, transactions and account statements.

Tech Stack

Java 17
Spring Boot
Spring Data JPA
H2 (in-memory DB)
Maven


How to run
Shellmvn spring-boot:run``Mostrar más líneas
The application starts on port 8080.
Base URL:
http://localhost:8080


API Endpoints
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


Testing

Unit tests for service layer
Integration test for transaction registration and persistence

Run tests with:
Shellmvn testMostrar más líneas

