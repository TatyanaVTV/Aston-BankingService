package ru.vtv.aston.intensive.account;

import lombok.Getter;
import lombok.NonNull;

import java.time.LocalDate;

import static ru.vtv.aston.intensive.account.Constants.SAVINGS_INTEREST_RATE;
import static ru.vtv.aston.intensive.account.Constants.moneyScale;

@Getter
public class SavingAccount extends DebitAccount implements InterestBearing {
    private LocalDate nextDueDate;

    public SavingAccount(@NonNull String accountHolder, @NonNull String accountNumber) {
        super(accountHolder, accountNumber);
        nextDueDate = LocalDate.now().plusMonths(1);
    }

    public void switchDueDate(LocalDate newDueDate) {
        this.nextDueDate = newDueDate;
    }

    @Override
    public void applyInterest() {
        System.out.printf("%s: %n", this.getAccountNumber());
        var interestsAmount = moneyScale(getBalance().multiply(SAVINGS_INTEREST_RATE));
        if (!LocalDate.now().isBefore(nextDueDate)) {
            System.out.printf(">>> Interest accrual on the remaining amount: +%.2f%n", interestsAmount);
            this.nextDueDate = LocalDate.now().plusMonths(1);
            deposit(interestsAmount);
        }
        System.out.printf(">>> Next interests due date: %s%n", nextDueDate);
    }
}