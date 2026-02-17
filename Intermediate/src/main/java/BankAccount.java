import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class BankAccount {
    private String accountNumber;
    private String accountHolder;
    private double balance;
    private List<Transaction> transactionHistory;

    public BankAccount(String accountNumber, String accountHolder, double initialBalance) {
        if (initialBalance < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative");
        }
        this.accountNumber = accountNumber;
        this.accountHolder = accountHolder;
        this.balance = initialBalance;
        this.transactionHistory = new ArrayList<>();
        if (initialBalance > 0) {
            addTransaction("INITIAL_DEPOSIT", initialBalance, "Initial deposit");
        }
    }

    public synchronized boolean deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }
        balance += amount;
        addTransaction("DEPOSIT", amount, "Deposit");
        return true;
    }

    public synchronized boolean withdraw(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }
        if (amount > balance) {
            return false; // Insufficient funds
        }
        balance -= amount;
        addTransaction("WITHDRAWAL", amount, "Withdrawal");
        return true;
    }

    public double getBalance() {
        return balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getAccountHolder() {
        return accountHolder;
    }

    public List<Transaction> getTransactionHistory() {
        return new ArrayList<>(transactionHistory);
    }

    private void addTransaction(String type, double amount, String description) {
        Transaction transaction = new Transaction(type, amount, balance, description);
        transactionHistory.add(transaction);
    }

    public void printTransactionHistory() {
        System.out.println("\n=== Transaction History for Account: " + accountNumber + " ===");
        System.out.println("Account Holder: " + accountHolder);
        System.out.println("Current Balance: $" + String.format("%.2f", balance));
        System.out.println("\nTransactions:");
        System.out.println("---------------------------------------------------------------");
        
        if (transactionHistory.isEmpty()) {
            System.out.println("No transactions yet.");
        } else {
            for (Transaction transaction : transactionHistory) {
                System.out.println(transaction);
            }
        }
        System.out.println("---------------------------------------------------------------\n");
    }

    public static class Transaction {
        private String type;
        private double amount;
        private double balanceAfter;
        private String description;
        private LocalDateTime timestamp;

        public Transaction(String type, double amount, double balanceAfter, String description) {
            this.type = type;
            this.amount = amount;
            this.balanceAfter = balanceAfter;
            this.description = description;
            this.timestamp = LocalDateTime.now();
        }

        public String getType() {
            return type;
        }

        public double getAmount() {
            return amount;
        }

        public double getBalanceAfter() {
            return balanceAfter;
        }

        public String getDescription() {
            return description;
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }

        @Override
        public String toString() {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            return String.format("[%s] %-18s $%-10.2f Balance: $%.2f - %s",
                    timestamp.format(formatter),
                    type,
                    amount,
                    balanceAfter,
                    description);
        }
    }
}
