package ru.vtv.aston.intensive.service;

import ru.vtv.aston.intensive.account.BankAccount;
import ru.vtv.aston.intensive.account.InterestBearing;
import ru.vtv.aston.intensive.account.NotEnoughFundsToCompleteTransactionException;

import java.math.BigDecimal;
import java.util.List;

public class TransactionProcessor {
    void processTransaction(List<BankAccount> accounts, BigDecimal amount) {
        accounts.forEach(account -> {
            try {
                System.out.printf("%s: %n", account.getAccountNumber());
                account.withdraw(amount);
            } catch (IllegalArgumentException | NotEnoughFundsToCompleteTransactionException ex) {
                System.out.println(ex.getLocalizedMessage());
            }
        });
    }

    void applyInterests(List<BankAccount> accounts) {
        accounts.stream()
                .filter(account -> InterestBearing.class.isAssignableFrom(account.getClass()))
                .map(account -> (InterestBearing) account)
                .forEach(InterestBearing::applyInterest);
    }
}
