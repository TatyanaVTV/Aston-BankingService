package ru.vtv.aston.intensive.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.vtv.aston.intensive.account.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class TransactionProcessorTest {
    public static List<BankAccount> accounts;
    public static Random RND = new Random();

    @BeforeAll
    public static void init() {
        accounts = new ArrayList<>();

        for (int i = 1; i < 6; i++) {
            var debitAccount = new DebitAccount(
                    "Debit account holder #" + i,
                    "Debit #" + i
            );
            var creditAccount = new CreditAccount(
                    "Credit account holder #" + i,
                    "Credit #" + i,
                    BigDecimal.valueOf(5000.00 * i)
            );
            var savingsAccount = new SavingAccount(
                    "Saving account holder #" + i,
                    "Saving #" + i);
            savingsAccount.switchDueDate(i % 2 == 0 ?
                    LocalDate.now().minusDays(1) :
                    LocalDate.now().plusMonths(1));

            accounts.add(debitAccount);
            accounts.add(creditAccount);
            accounts.add(savingsAccount);
        }

        accounts.forEach(account ->
                account.deposit(BigDecimal.valueOf(1000.00 * RND.nextInt(2, 20))));
    }

    private BigDecimal getRandomAmountForOperation() {
        return BigDecimal.valueOf(RND.nextDouble(1000.00, 5000.00));
    }

    @Test
    void processTransaction() {
        var transactionProcessor = new TransactionProcessor();
        transactionProcessor.processTransaction(accounts, getRandomAmountForOperation());
    }

    @Test
    void applyInterests() {
        var transactionProcessor = new TransactionProcessor();
        transactionProcessor.applyInterests(accounts);
    }
}