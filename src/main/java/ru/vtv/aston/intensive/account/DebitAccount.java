package ru.vtv.aston.intensive.account;

import lombok.NonNull;
import ru.vtv.aston.intensive.validation.DebitAccountWithdrawValidator;
import ru.vtv.aston.intensive.validation.NotNullPositiveValueValidator;

import java.math.BigDecimal;

public class DebitAccount extends BankAccount {
    public DebitAccount(@NonNull String accountHolder, @NonNull String accountNumber) {
        super(accountHolder, accountNumber);
        addWithdrawValidator(new NotNullPositiveValueValidator());
        addWithdrawValidator(new DebitAccountWithdrawValidator());
    }

    @Override
    public void withdraw(BigDecimal amount) throws NotEnoughFundsToCompleteTransactionException {
        if (!withdrawValidators.stream().allMatch(validator -> validator.validate(amount))) {
            throw new IllegalArgumentException(String.format("%.2f is NOT valid amount for this operation.%n", amount));
        }

        if (getBalance().compareTo(amount) < 0) {
            throw new NotEnoughFundsToCompleteTransactionException();
        }

        setBalance(getBalance().subtract(amount));
        System.out.printf(">>> Withdrawing %.2f from account (new balance: %.2f)%n", amount, getBalance());
    }
}