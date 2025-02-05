package ru.vtv.aston.intensive.account;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BankAccountTest {
    private BankAccount testAccount;

    @Test
    void deposit_nullValue_ShouldThrown_IllegalArgumentException() {
        testAccount = new DebitAccount("Test holder", "");
        assertThrows(IllegalArgumentException.class, () -> testAccount.deposit(null));
    }
}