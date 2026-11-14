package org.pshenko.springcore.account;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AccountProperties {
    private final int defaultAmount;
    private final double transferCommission;

    public AccountProperties(
        @Value("${account.default-amount:500}") int defaultAmount,
        @Value("${account.transfer-commission:0.02}") double transferCommission
    ) {
        this.defaultAmount = defaultAmount;
        this.transferCommission = transferCommission;
    }

    public int getDefaultAmount() {
        return defaultAmount;
    }

    public double getTransferCommission() {
        return transferCommission;
    }

    @PostConstruct
    public void init() {
        if (defaultAmount < 0) {
            throw new IllegalArgumentException("defaultAmount cannot be negative");
        }
        if (!Double.isFinite(transferCommission) || transferCommission < 0.0 || transferCommission > 1.0) {
            throw new IllegalArgumentException("transferCommission must be between 0.0 and 1.0");
        }
    }
}
