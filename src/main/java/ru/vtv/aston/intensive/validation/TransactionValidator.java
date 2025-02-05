package ru.vtv.aston.intensive.validation;

import java.math.BigDecimal;

public interface TransactionValidator {
    boolean validate(BigDecimal amount);
}
