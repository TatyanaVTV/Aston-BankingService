package ru.vtv.aston.intensive.validation;

import java.math.BigDecimal;

public class NotNullPositiveValueValidator implements TransactionValidator {
    @Override
    public boolean validate(BigDecimal amount) {
        return amount != null &&BigDecimal.ZERO.compareTo(amount) <= 0;
    }
}
