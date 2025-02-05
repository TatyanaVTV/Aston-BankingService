package ru.vtv.aston.intensive.account;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static ru.vtv.aston.intensive.account.Constants.SAVINGS_INTEREST_RATE;
import static ru.vtv.aston.intensive.account.Constants.moneyScale;

class SavingAccountTest {
    private static final BigDecimal INITIAL_BALANCE = moneyScale(BigDecimal.valueOf(10000.00));

    private SavingAccount testAccount;

    @BeforeEach
    public void setUp() {
        testAccount = new SavingAccount("Test holder", "42301810001234569874");
        testAccount.deposit(INITIAL_BALANCE);
    }

    @Test
    void applyInterest_beforeDueDate_shouldHaveInitialBalance() {
        testAccount.applyInterest();
        assertEquals(INITIAL_BALANCE, testAccount.getBalance());
    }

    @Test
    void applyInterest_onDueDate_shouldHave_interestsCharged() {
        testAccount.switchDueDate(LocalDate.now().minusDays(30));
        var interestsAmount = moneyScale(INITIAL_BALANCE.multiply(SAVINGS_INTEREST_RATE));
        testAccount.applyInterest();
        assertEquals(INITIAL_BALANCE.add(interestsAmount), testAccount.getBalance());
    }
}