package ru.vtv.aston.intensive.validation;

import java.math.BigDecimal;

import static ru.vtv.aston.intensive.account.Constants.DEBIT_ACC_MAX_TRANSACTION_AMOUNT;

public class DebitAccountWithdrawValidator implements TransactionValidator {
    @Override
    public boolean validate(BigDecimal amount) {
        return amount.compareTo(BigDecimal.valueOf(DEBIT_ACC_MAX_TRANSACTION_AMOUNT)) <= 0;
    }
}
