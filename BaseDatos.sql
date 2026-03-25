-- =========================================
-- DATABASE SCRIPT FOR BANKING MICROSERVICES
-- =========================================

-- ========================
-- CUSTOMER SERVICE TABLES
-- ========================

CREATE TABLE customers (
    customer_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    gender VARCHAR(20) NOT NULL,
    age INTEGER NOT NULL,
    identification VARCHAR(50) NOT NULL UNIQUE,
    address VARCHAR(255) NOT NULL,
    phone VARCHAR(50) NOT NULL,
    password VARCHAR(100) NOT NULL,
    active BOOLEAN NOT NULL
);

-- ========================
-- ACCOUNT SERVICE TABLES
-- ========================

CREATE TABLE accounts (
    account_id BIGSERIAL PRIMARY KEY,
    account_number VARCHAR(50) NOT NULL UNIQUE,
    account_type VARCHAR(30) NOT NULL,
    initial_balance NUMERIC(15,2) NOT NULL,
    current_balance NUMERIC(15,2) NOT NULL,
    active BOOLEAN NOT NULL,

    -- Logical reference to customer-service
    customer_id BIGINT NOT NULL
);

CREATE TABLE transactions (
    transaction_id BIGSERIAL PRIMARY KEY,
    account_id BIGINT NOT NULL,
    transaction_date TIMESTAMP NOT NULL,
    transaction_type VARCHAR(30) NOT NULL,
    amount NUMERIC(15,2) NOT NULL,
    balance_after_transaction NUMERIC(15,2) NOT NULL

    -- NOTE:
    -- No foreign key to customers table by design (microservices boundary)
);

-- Optional FK inside account-service boundary
ALTER TABLE transactions
ADD CONSTRAINT fk_transactions_account
FOREIGN KEY (account_id)
REFERENCES accounts(account_id);