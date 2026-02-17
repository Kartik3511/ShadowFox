# Bank Account Management System with JUnit Testing

A comprehensive Java-based bank account management system with full unit test coverage using JUnit 5.

## Features

- **Account Management**: Create accounts with account number, holder name, and initial balance
- **Deposit**: Add money to the account with validation
- **Withdrawal**: Withdraw money with insufficient funds checking
- **Balance Inquiry**: Check current account balance
- **Transaction History**: View complete history of all transactions with timestamps
- **Thread Safety**: Synchronized deposit and withdrawal operations
- **Input Validation**: Comprehensive error handling for invalid operations

## Project Structure

```
├── BankAccount.java                    # Core bank account class
├── BankAccountManagementSystem.java    # Main application with CLI
├── BankAccountTest.java                # JUnit test suite
└── README.md                           # This file
```

## Classes

### BankAccount
Main class containing:
- Account details (number, holder, balance)
- Transaction methods (deposit, withdraw)
- Transaction history tracking
- Inner Transaction class for transaction records

### BankAccountManagementSystem
Interactive command-line application for:
- Creating bank accounts
- Performing deposits and withdrawals
- Checking balance
- Viewing transaction history

### BankAccountTest
Comprehensive JUnit test suite with 20+ test cases covering:
- Account creation scenarios
- Deposit operations
- Withdrawal operations
- Balance inquiry
- Transaction history
- Edge cases and error handling
- Thread safety

## Prerequisites

- Java 11 or higher
- Maven 3.6 or higher

## Quick Setup

### 1. Install Dependencies
```bash
mvn clean install
```

This will automatically download JUnit 5 and compile everything.

### 2. Run Tests
```bash
mvn test
```

### 3. Run the Application
```bash
mvn exec:java -Dexec.mainClass="BankAccountManagementSystem"
```

## Maven Directory Structure


```
Intermediate/
├── pom.xml
├── README.md
└── src/
    ├── main/java/          # Main source files
    │   ├── BankAccount.java
    │   └── BankAccountManagementSystem.java
    └── test/java/          # Test files
        └── BankAccountTest.java
```

## Alternative: Manual Compilation

If not using Maven:
```bash
# Compile main classes
javac src/main/java/*.java

# Run application
java -cp src/main/java BankAccountManagementSystem
```

## Test Coverage

The test suite includes:

### Account Creation Tests
- Valid account creation
- Zero balance accounts
- Negative balance validation

### Deposit Tests
- Valid deposits
- Multiple deposits
- Zero and negative amount validation

### Withdrawal Tests
- Valid withdrawals
- Insufficient funds handling
- Exact balance withdrawal
- Zero and negative amount validation

### Balance Inquiry Tests
- Balance tracking across operations
- Balance consistency

### Transaction History Tests
- Initial transaction recording
- Deposit transaction tracking
- Withdrawal transaction tracking
- Multiple operations sequencing
- History immutability
- Timestamp verification

### Edge Cases
- Thread safety with concurrent operations
- Zero balance operations
- Boundary value testing

## Example Usage

```java
// Create account
BankAccount account = new BankAccount("ACC001", "John Doe", 1000.0);

// Deposit
account.deposit(500.0);  // Balance: 1500.0

// Withdraw
account.withdraw(300.0); // Balance: 1200.0

// Check balance
double balance = account.getBalance(); // 1200.0

// Get transaction history
List<BankAccount.Transaction> history = account.getTransactionHistory();

// Print transaction history
account.printTransactionHistory();
```

## Error Handling

The system handles various error scenarios:
- Negative initial balance → IllegalArgumentException
- Zero or negative deposit → IllegalArgumentException
- Zero or negative withdrawal → IllegalArgumentException
- Insufficient funds → Returns false, balance unchanged

## Thread Safety

Deposit and withdrawal operations are synchronized to ensure thread-safe concurrent access.

## Author

Created for ShadowFox Intermediate Level Tasks

