package org.pshenko.springcore.account;

public record TransferResult(
    int amount,
    int commission,
    int amountAfterCommission
) {
}
