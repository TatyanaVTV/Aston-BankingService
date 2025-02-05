package ru.vtv.aston.intensive.account;

public class NotEnoughFundsToCompleteTransactionException extends RuntimeException {
    public NotEnoughFundsToCompleteTransactionException() {
        super("There are not enough funds in the account to complete the transaction!");
    }
}