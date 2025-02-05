package ru.vtv.aston.intensive.validation;

import java.math.BigDecimal;

import static ru.vtv.aston.intensive.account.Constants.CREDIT_ACC_MAX_TRANSACTION_AMOUNT;
import static ru.vtv.aston.intensive.account.Constants.moneyScale;

public class CreditAccountWithdrawValidator implements TransactionValidator {
    @Override
    public boolean validate(BigDecimal amount) {
        return amount.compareTo(moneyScale(BigDecimal.valueOf(CREDIT_ACC_MAX_TRANSACTION_AMOUNT))) <= 0;
    }
}
