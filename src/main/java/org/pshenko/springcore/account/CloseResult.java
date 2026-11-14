package org.pshenko.springcore.account;

public record CloseResult(
    int closedAccountId,
    int targetAccountId,
    int transferredAmount
) {
}
