import java.util.Scanner;

public class BankAccountManagementSystem {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        BankAccount account = null;

        System.out.println("=================================");
        System.out.println("Bank Account Management System");
        System.out.println("=================================\n");

        // Create account
        System.out.print("Enter Account Number: ");
        String accountNumber = scanner.nextLine();
        
        System.out.print("Enter Account Holder Name: ");
        String accountHolder = scanner.nextLine();
        
        System.out.print("Enter Initial Balance: $");
        double initialBalance = scanner.nextDouble();
        scanner.nextLine(); // Consume newline

        try {
            account = new BankAccount(accountNumber, accountHolder, initialBalance);
            System.out.println("\nAccount created successfully!");
            System.out.println("Account Number: " + account.getAccountNumber());
            System.out.println("Account Holder: " + account.getAccountHolder());
            System.out.println("Initial Balance: $" + String.format("%.2f", account.getBalance()));
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
            scanner.close();
            return;
        }

        // Main menu
        boolean running = true;
        while (running) {
            System.out.println("\n===== Main Menu =====");
            System.out.println("1. Deposit");
            System.out.println("2. Withdraw");
            System.out.println("3. Check Balance");
            System.out.println("4. View Transaction History");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1: // Deposit
                    System.out.print("Enter deposit amount: $");
                    double depositAmount = scanner.nextDouble();
                    scanner.nextLine();
                    
                    try {
                        if (account.deposit(depositAmount)) {
                            System.out.println("Deposit successful!");
                            System.out.println("New Balance: $" + String.format("%.2f", account.getBalance()));
                        }
                    } catch (IllegalArgumentException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;

                case 2: // Withdraw
                    System.out.print("Enter withdrawal amount: $");
                    double withdrawAmount = scanner.nextDouble();
                    scanner.nextLine();
                    
                    try {
                        if (account.withdraw(withdrawAmount)) {
                            System.out.println("Withdrawal successful!");
                            System.out.println("New Balance: $" + String.format("%.2f", account.getBalance()));
                        } else {
                            System.out.println("Error: Insufficient funds!");
                            System.out.println("Current Balance: $" + String.format("%.2f", account.getBalance()));
                        }
                    } catch (IllegalArgumentException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;

                case 3: // Check Balance
                    System.out.println("\n===== Balance Inquiry =====");
                    System.out.println("Account Number: " + account.getAccountNumber());
                    System.out.println("Account Holder: " + account.getAccountHolder());
                    System.out.println("Current Balance: $" + String.format("%.2f", account.getBalance()));
                    break;

                case 4: // View Transaction History
                    account.printTransactionHistory();
                    break;

                case 5: // Exit
                    System.out.println("\nThank you for using Bank Account Management System!");
                    System.out.println("Final Balance: $" + String.format("%.2f", account.getBalance()));
                    running = false;
                    break;

                default:
                    System.out.println("Invalid option! Please try again.");
            }
        }

        scanner.close();
    }
}
