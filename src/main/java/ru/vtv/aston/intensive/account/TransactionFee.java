package ru.vtv.aston.intensive.account;

import java.math.BigDecimal;

public interface TransactionFee {
    BigDecimal applyFee(BigDecimal amount);
}
