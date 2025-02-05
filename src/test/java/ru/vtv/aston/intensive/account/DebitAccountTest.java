package ru.vtv.aston.intensive.account;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static ru.vtv.aston.intensive.account.Constants.*;

class DebitAccountTest {
    private static final BigDecimal INITIAL_BALANCE = BigDecimal.valueOf(10000.00);

    private DebitAccount testAccount;

    @BeforeEach
    public void setUp() {
        testAccount = new DebitAccount("Test holder", "42301810001234569874");
        testAccount.deposit(INITIAL_BALANCE);
    }

    @Test
    void deposit_nullValue_shouldBe_success() {
        assertThrows(IllegalArgumentException.class, () -> testAccount.deposit(null));
    }

    @Test
    void deposit_positiveValue_shouldBe_success() {
        var expectedAmount = moneyScale(INITIAL_BALANCE.add(BigDecimal.valueOf(75000.00)));
        testAccount.deposit(BigDecimal.valueOf(75000.00));
        assertEquals(expectedAmount, testAccount.getBalance());
    }

    @Test
    void deposit_negativeValue_shouldThrow_IllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> testAccount.deposit(BigDecimal.valueOf(-1000.00)));
    }

    @Test
    void withdraw_lessThanBalance_shouldBe_success() {
        var testAmount = INITIAL_BALANCE.divide(BigDecimal.valueOf(2), ROUND_MODE);
        var expectedAmount = moneyScale(INITIAL_BALANCE.subtract(testAmount));
        testAccount.withdraw(testAmount);
        assertEquals(expectedAmount, testAccount.getBalance());
    }

    @Test
    void withdraw_fullBalance_shouldBe_success() {
        testAccount.withdraw(INITIAL_BALANCE);
        assertEquals(moneyScale(BigDecimal.ZERO), testAccount.getBalance());
    }

    @Test
    void withdraw_moreThanBalance_shouldThrow_NotEnoughFundsToCompleteTransactionException() {
        var amount = INITIAL_BALANCE.divide(BigDecimal.valueOf(2), ROUND_MODE);
        testAccount.withdraw(amount);
        assertThrows(NotEnoughFundsToCompleteTransactionException.class,
                () -> testAccount.withdraw(testAccount.getBalance().add(BigDecimal.ONE)));
    }

    @Test
    void withdraw_notValidByValidator_shouldThrow_IllegalArgumentException() {
        testAccount.deposit(BigDecimal.valueOf(CREDIT_ACC_MAX_TRANSACTION_AMOUNT));

        var balanceBeforeOperation = testAccount.getBalance();
        var withdrawAmount = BigDecimal.valueOf(DEBIT_ACC_MAX_TRANSACTION_AMOUNT).add(BigDecimal.ONE);

        assertEquals(balanceBeforeOperation, testAccount.getBalance());
        assertThrows(IllegalArgumentException.class, () -> testAccount.withdraw(withdrawAmount));
    }

    @Test
    void withdraw_validByValidator_shouldBe_success() {
        testAccount.deposit(BigDecimal.valueOf(CREDIT_ACC_MAX_TRANSACTION_AMOUNT));

        var balanceBeforeOperation = testAccount.getBalance();
        var withdrawAmount = BigDecimal.valueOf(DEBIT_ACC_MAX_TRANSACTION_AMOUNT);

        testAccount.withdraw(withdrawAmount);
        assertEquals(balanceBeforeOperation.subtract(withdrawAmount), testAccount.getBalance());
    }

    @Test
    void withdraw_negativeValue_shouldThrow_IllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> testAccount.withdraw(BigDecimal.valueOf(-1000.00)));
    }
}