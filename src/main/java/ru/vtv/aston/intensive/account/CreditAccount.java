package ru.vtv.aston.intensive.account;

import lombok.*;
import ru.vtv.aston.intensive.validation.CreditAccountWithdrawValidator;
import ru.vtv.aston.intensive.validation.NotNullPositiveValueValidator;

import java.math.BigDecimal;

import static ru.vtv.aston.intensive.account.Constants.*;

@Setter
@Getter
@ToString
public class CreditAccount extends BankAccount implements TransactionFee {
    private BigDecimal creditLimit;

    public CreditAccount(@NonNull String accountHolder, @NonNull String accountNumber, BigDecimal creditLimit) {
        super(accountHolder, accountNumber);
        this.creditLimit = creditLimit;
        addWithdrawValidator(new NotNullPositiveValueValidator());
        addWithdrawValidator(new CreditAccountWithdrawValidator());
    }

    @Override
    public BigDecimal applyFee(BigDecimal amount) {
        var fee = calculatePossibleFeeAmount(amount);
        setBalance(getBalance().subtract(fee));
        System.out.printf(">>> Charging fee for withdrawing from account (%.2f -> %.2f) %n", CRED_ACC_WITHDRAW_FEE, fee);
        return fee;
    }

    private BigDecimal calculatePossibleFeeAmount(BigDecimal amount) {
        return moneyScale(amount.multiply(CRED_ACC_WITHDRAW_FEE));
    }

    @Override
    public void withdraw(BigDecimal amount) throws NotEnoughFundsToCompleteTransactionException {
        if (!withdrawValidators.stream().allMatch(validator -> validator.validate(amount))) {
            throw new IllegalArgumentException(String.format("%.2f is NOT valid amount for this operation!%n", amount));
        }

        var possibleFeeAmount = calculatePossibleFeeAmount(amount);
        var maxAmountToWithdraw = getBalance().add(creditLimit);
        var requestedAmount = amount.add(possibleFeeAmount);

        if (maxAmountToWithdraw.compareTo(requestedAmount) < 0) throw new NotEnoughFundsToCompleteTransactionException();

        setBalance(getBalance().subtract(amount));
        System.out.printf(">>> Withdrawing %.2f from account (new balance: %.2f)%n", amount, getBalance());
        applyFee(amount);
    }
}