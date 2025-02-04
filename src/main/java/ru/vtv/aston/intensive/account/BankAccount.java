package ru.vtv.aston.intensive.account;

import lombok.*;
import ru.vtv.aston.intensive.validation.NotNullPositiveValueValidator;
import ru.vtv.aston.intensive.validation.TransactionValidator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static ru.vtv.aston.intensive.account.Constants.*;

@Getter
@RequiredArgsConstructor
@ToString
public abstract class BankAccount {
    @NonNull
    private String accountHolder;
    @NonNull
    private String accountNumber;

    protected List<TransactionValidator> withdrawValidators = new ArrayList<>();
    protected List<TransactionValidator> depositValidators = List.of(new NotNullPositiveValueValidator());

    @Setter
    private BigDecimal balance = moneyScale(BigDecimal.ZERO);

    public abstract void withdraw(BigDecimal amount) throws NotEnoughFundsToCompleteTransactionException;

    public void deposit(BigDecimal amount) {
        if (!depositValidators.stream().allMatch(validator -> validator.validate(amount))) {
            throw new IllegalArgumentException(String.format("%.2f is NOT valid amount for this operation!%n", amount));
        }
        balance = balance.add(amount);
        System.out.printf("<<< Transferring %.2f into account (new balance: %.2f)%n", amount, getBalance());
    }

    protected void addWithdrawValidator(TransactionValidator validator) {
        withdrawValidators.add(validator);
    }
}