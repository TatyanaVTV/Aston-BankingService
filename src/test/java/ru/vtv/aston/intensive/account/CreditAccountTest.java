package ru.vtv.aston.intensive.account;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.vtv.aston.intensive.account.Constants.*;

class CreditAccountTest {
    private static final BigDecimal INITIAL_BALANCE = BigDecimal.valueOf(2000.00);
    private static final BigDecimal CREDIT_LIMIT = BigDecimal.valueOf(2000.00);

    private CreditAccount testAccount;

    @BeforeEach
    public void setUp() {
        testAccount = new CreditAccount("Test holder", "42301810001234569874", CREDIT_LIMIT);
        testAccount.deposit(INITIAL_BALANCE);
    }

    @Test
    void applyFee_shouldReturn_CorrectValue() {
        var expectedAmount = moneyScale(INITIAL_BALANCE.multiply(CRED_ACC_WITHDRAW_FEE));
        assertEquals(expectedAmount, testAccount.applyFee(INITIAL_BALANCE));
        assertEquals(moneyScale(INITIAL_BALANCE.subtract(expectedAmount)), testAccount.getBalance());
    }

    @Test
    void deposit_positiveValue_shouldBe_success() {
        var testAmount = BigDecimal.valueOf(75000.00);
        var expectedAmount = moneyScale(INITIAL_BALANCE.add(testAmount));
        testAccount.deposit(testAmount);
        assertEquals(expectedAmount, testAccount.getBalance());
    }

    @Test
    void deposit_negativeValue_shouldThrow_IllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> testAccount.deposit(BigDecimal.valueOf(-1000.00)));
    }

    @Test
    void withdraw_lessThanBalance_shouldBe_success() {
        var testAmount = moneyScale(INITIAL_BALANCE.divide(BigDecimal.valueOf(2), ROUND_MODE));
        var testAmountFee = testAmount.multiply(CRED_ACC_WITHDRAW_FEE);
        var expectedAmount = moneyScale(INITIAL_BALANCE.subtract(testAmount).subtract(testAmountFee));
        testAccount.withdraw(testAmount);
        assertEquals(expectedAmount, testAccount.getBalance());
    }

    @Test
    void withdraw_fullBalance_shouldBe_success() {
        var expectedValue = moneyScale(BigDecimal.ZERO.subtract(INITIAL_BALANCE.multiply(CRED_ACC_WITHDRAW_FEE)));
        testAccount.withdraw(INITIAL_BALANCE);
        assertEquals(expectedValue, testAccount.getBalance());
    }

    @Test
    void withdraw_moreThanBalanceAndCreditLimit_shouldThrow_NotEnoughFundsToCompleteTransactionException() {
        var maxAmount = INITIAL_BALANCE.add(CREDIT_LIMIT);
        assertThrows(NotEnoughFundsToCompleteTransactionException.class,
                () -> testAccount.withdraw(maxAmount.add(BigDecimal.ONE)));
    }

    @Test
    void withdraw_notValidByValidator_shouldThrow_IllegalArgumentException() {
        testAccount.deposit(BigDecimal.valueOf(CREDIT_ACC_MAX_TRANSACTION_AMOUNT));

        var balanceBeforeOperation = testAccount.getBalance();
        var withdrawAmount = BigDecimal.valueOf(CREDIT_ACC_MAX_TRANSACTION_AMOUNT).add(BigDecimal.ONE);

        assertEquals(balanceBeforeOperation, testAccount.getBalance());
        assertThrows(IllegalArgumentException.class, () -> testAccount.withdraw(withdrawAmount));
    }

    @Test
    void withdraw_validByValidator_shouldBe_success() {
        testAccount.deposit(BigDecimal.valueOf(CREDIT_ACC_MAX_TRANSACTION_AMOUNT));

        var balanceBeforeOperation = testAccount.getBalance();
        var withdrawAmount = BigDecimal.valueOf(CREDIT_ACC_MAX_TRANSACTION_AMOUNT);
        var feeAmount = withdrawAmount.multiply(CRED_ACC_WITHDRAW_FEE);

        testAccount.withdraw(withdrawAmount);
        assertEquals(moneyScale(balanceBeforeOperation.subtract(withdrawAmount).subtract(feeAmount)), testAccount.getBalance());
    }

    @Test
    void withdraw_negativeValue_shouldThrow_IllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> testAccount.withdraw(BigDecimal.valueOf(-1000.00)));
    }
}