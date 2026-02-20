import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

@DisplayName("Bank Account Management System Tests")
public class BankAccountTest {
    private BankAccount account;

    @BeforeEach
    public void setUp() {
        account = new BankAccount("ACC001", "John Doe", 1000.0);
    }

    @Test
    @DisplayName("Test account creation with valid initial balance")
    public void testAccountCreation() {
        assertEquals("ACC001", account.getAccountNumber());
        assertEquals("John Doe", account.getAccountHolder());
        assertEquals(1000.0, account.getBalance(), 0.01);
    }

    @Test
    @DisplayName("Test account creation with zero initial balance")
    public void testAccountCreationWithZeroBalance() {
        BankAccount zeroAccount = new BankAccount("ACC002", "Jane Smith", 0.0);
        assertEquals(0.0, zeroAccount.getBalance(), 0.01);
    }

    @Test
    @DisplayName("Test account creation with negative balance throws exception")
    public void testAccountCreationWithNegativeBalance() {
        assertThrows(IllegalArgumentException.class, () -> {
            new BankAccount("ACC003", "Invalid User", -100.0);
        });
    }

    @Test
    @DisplayName("Test deposit with valid amount")
    public void testDeposit() {
        assertTrue(account.deposit(500.0));
        assertEquals(1500.0, account.getBalance(), 0.01);
    }

    @Test
    @DisplayName("Test multiple deposits")
    public void testMultipleDeposits() {
        account.deposit(200.0);
        account.deposit(300.0);
        account.deposit(500.0);
        assertEquals(2000.0, account.getBalance(), 0.01);
    }

    @Test
    @DisplayName("Test deposit with zero amount throws exception")
    public void testDepositZeroAmount() {
        assertThrows(IllegalArgumentException.class, () -> {
            account.deposit(0.0);
        });
    }

    @Test
    @DisplayName("Test deposit with negative amount throws exception")
    public void testDepositNegativeAmount() {
        assertThrows(IllegalArgumentException.class, () -> {
            account.deposit(-100.0);
        });
    }

    @Test
    @DisplayName("Test withdrawal with valid amount")
    public void testWithdrawal() {
        assertTrue(account.withdraw(300.0));
        assertEquals(700.0, account.getBalance(), 0.01);
    }

    @Test
    @DisplayName("Test multiple withdrawals")
    public void testMultipleWithdrawals() {
        account.withdraw(100.0);
        account.withdraw(200.0);
        account.withdraw(150.0);
        assertEquals(550.0, account.getBalance(), 0.01);
    }

    @Test
    @DisplayName("Test withdrawal with insufficient funds")
    public void testWithdrawalInsufficientFunds() {
        assertFalse(account.withdraw(1500.0));
        assertEquals(1000.0, account.getBalance(), 0.01); // Balance should remain unchanged
    }

    @Test
    @DisplayName("Test withdrawal with exact balance")
    public void testWithdrawalExactBalance() {
        assertTrue(account.withdraw(1000.0));
        assertEquals(0.0, account.getBalance(), 0.01);
    }

    @Test
    @DisplayName("Test withdrawal with zero amount throws exception")
    public void testWithdrawalZeroAmount() {
        assertThrows(IllegalArgumentException.class, () -> {
            account.withdraw(0.0);
        });
    }

    @Test
    @DisplayName("Test withdrawal with negative amount throws exception")
    public void testWithdrawalNegativeAmount() {
        assertThrows(IllegalArgumentException.class, () -> {
            account.withdraw(-50.0);
        });
    }

    @Test
    @DisplayName("Test balance inquiry")
    public void testBalanceInquiry() {
        double initialBalance = account.getBalance();
        assertEquals(1000.0, initialBalance, 0.01);
        
        account.deposit(500.0);
        assertEquals(1500.0, account.getBalance(), 0.01);
        
        account.withdraw(300.0);
        assertEquals(1200.0, account.getBalance(), 0.01);
    }

    @Test
    @DisplayName("Test transaction history after account creation")
    public void testTransactionHistoryInitial() {
        List<BankAccount.Transaction> history = account.getTransactionHistory();
        assertEquals(1, history.size()); // Initial deposit
        assertEquals("INITIAL_DEPOSIT", history.get(0).getType());
        assertEquals(1000.0, history.get(0).getAmount(), 0.01);
    }

    @Test
    @DisplayName("Test transaction history after deposit")
    public void testTransactionHistoryAfterDeposit() {
        account.deposit(500.0);
        List<BankAccount.Transaction> history = account.getTransactionHistory();
        assertEquals(2, history.size()); // Initial + 1 deposit
        
        BankAccount.Transaction lastTransaction = history.get(history.size() - 1);
        assertEquals("DEPOSIT", lastTransaction.getType());
        assertEquals(500.0, lastTransaction.getAmount(), 0.01);
        assertEquals(1500.0, lastTransaction.getBalanceAfter(), 0.01);
    }

    @Test
    @DisplayName("Test transaction history after withdrawal")
    public void testTransactionHistoryAfterWithdrawal() {
        account.withdraw(300.0);
        List<BankAccount.Transaction> history = account.getTransactionHistory();
        assertEquals(2, history.size()); // Initial + 1 withdrawal
        
        BankAccount.Transaction lastTransaction = history.get(history.size() - 1);
        assertEquals("WITHDRAWAL", lastTransaction.getType());
        assertEquals(300.0, lastTransaction.getAmount(), 0.01);
        assertEquals(700.0, lastTransaction.getBalanceAfter(), 0.01);
    }

    @Test
    @DisplayName("Test transaction history with multiple operations")
    public void testTransactionHistoryMultipleOperations() {
        account.deposit(500.0);
        account.withdraw(200.0);
        account.deposit(300.0);
        account.withdraw(100.0);
        
        List<BankAccount.Transaction> history = account.getTransactionHistory();
        assertEquals(5, history.size()); // Initial + 4 operations
        
        // Verify the sequence
        assertEquals("INITIAL_DEPOSIT", history.get(0).getType());
        assertEquals("DEPOSIT", history.get(1).getType());
        assertEquals("WITHDRAWAL", history.get(2).getType());
        assertEquals("DEPOSIT", history.get(3).getType());
        assertEquals("WITHDRAWAL", history.get(4).getType());
        
        // Verify final balance
        assertEquals(1500.0, account.getBalance(), 0.01);
    }

    @Test
    @DisplayName("Test transaction history immutability")
    public void testTransactionHistoryImmutability() {
        List<BankAccount.Transaction> history1 = account.getTransactionHistory();
        int initialSize = history1.size();
        
        account.deposit(100.0);
        
        // Original list should not be modified
        assertEquals(initialSize, history1.size());
        
        // New list should have additional transaction
        List<BankAccount.Transaction> history2 = account.getTransactionHistory();
        assertEquals(initialSize + 1, history2.size());
    }

    @Test
    @DisplayName("Test transaction timestamps")
    public void testTransactionTimestamps() throws InterruptedException {
        account.deposit(100.0);
        Thread.sleep(10); // Small delay
        account.withdraw(50.0);
        
        List<BankAccount.Transaction> history = account.getTransactionHistory();
        assertTrue(history.get(1).getTimestamp().isBefore(history.get(2).getTimestamp()) ||
                   history.get(1).getTimestamp().isEqual(history.get(2).getTimestamp()));
    }

    @Test
    @DisplayName("Test account with zero balance after creation")
    public void testZeroBalanceAccount() {
        BankAccount zeroAccount = new BankAccount("ACC999", "Zero Balance", 0.0);
        assertEquals(0.0, zeroAccount.getBalance(), 0.01);
        
        zeroAccount.deposit(100.0);
        assertEquals(100.0, zeroAccount.getBalance(), 0.01);
        
        assertTrue(zeroAccount.withdraw(100.0));
        assertEquals(0.0, zeroAccount.getBalance(), 0.01);
    }

    @Test
    @DisplayName("Test concurrent deposits (thread safety)")
    public void testConcurrentDeposits() throws InterruptedException {
        BankAccount concurrentAccount = new BankAccount("ACC100", "Concurrent User", 0.0);
        
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                concurrentAccount.deposit(10.0);
            }
        });
        
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                concurrentAccount.deposit(10.0);
            }
        });
        
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        
        assertEquals(2000.0, concurrentAccount.getBalance(), 0.01);
    }
}
