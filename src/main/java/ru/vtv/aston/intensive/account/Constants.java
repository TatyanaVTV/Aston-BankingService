package ru.vtv.aston.intensive.account;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static java.math.RoundingMode.HALF_EVEN;

public abstract class Constants {
    public static int SCALE = 2;
    public static RoundingMode ROUND_MODE = HALF_EVEN;

    public static BigDecimal CRED_ACC_WITHDRAW_FEE = BigDecimal.valueOf(0.01).setScale(SCALE, ROUND_MODE);
    public static BigDecimal SAVINGS_INTEREST_RATE = BigDecimal.valueOf(0.16).setScale(SCALE, ROUND_MODE);

    public static double DEBIT_ACC_MAX_TRANSACTION_AMOUNT = 10_000;
    public static double CREDIT_ACC_MAX_TRANSACTION_AMOUNT = 5_000;

    public static BigDecimal moneyScale(BigDecimal amount) {
        return amount.setScale(SCALE, ROUND_MODE);
    }
}
