package org.pshenko.springcore.account;

import org.pshenko.springcore.exception.BankException;
import org.pshenko.springcore.user.User;
import org.pshenko.springcore.user.UserService;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AccountService {
    private final Map<Integer, Account> accounts;
    private final AccountProperties properties;
    private final UserService userService;
    private int idCounter;

    public AccountService(AccountProperties properties, UserService userService) {
        this.properties = properties;
        this.userService = userService;
        this.accounts = new HashMap<>();
        idCounter = 0;
    }

    public Account create(int userId) {
        User user = userService.findById(userId)
            .orElseThrow(() -> new BankException("user with id=%d not found".formatted(userId)));
        idCounter++;
        Account account = new Account(idCounter, userId, properties.getDefaultAmount());
        user.getAccountList().add(account);
        accounts.put(idCounter, account);
        return account;
    }

    public Account deposit(int accountId, int amount) {
        validateAmount(amount);
        Account account = getAccountOrThrow(accountId);
        int existingMoney = account.getMoneyAmount();
        account.setMoneyAmount(existingMoney + amount);
        return account;
    }

    public Account withdraw(int accountId, int amount) {
        validateAmount(amount);
        Account account = getAccountOrThrow(accountId);
        int existingMoney = account.getMoneyAmount();
        if (existingMoney < amount) {
            throw new BankException("insufficient funds on account id=%d, moneyAmount=%d, attempted withdraw=%d"
                .formatted(accountId, existingMoney, amount));
        }
        account.setMoneyAmount(existingMoney - amount);
        return account;
    }

    public TransferResult transfer(int fromAccountId, int toAccountId, int amount) {
        validateAmount(amount);
        if (fromAccountId == toAccountId) {
            throw new BankException("cannot transfer to the same account id=%d".formatted(fromAccountId));
        }
        Account fromAccount = getAccountOrThrow(fromAccountId);
        Account toAccount = getAccountOrThrow(toAccountId);
        if (fromAccount.getMoneyAmount() < amount) {
            throw new BankException("insufficient funds on source account id=%d, moneyAmount=%d, attempted transfer=%d"
                .formatted(fromAccountId, fromAccount.getMoneyAmount(), amount));
        }
        double transferCommission = fromAccount.getUserId() == toAccount.getUserId()
            ? 0
            : properties.getTransferCommission();
        int received = (int) Math.round(amount * (1 - transferCommission));
        int commission = amount - received;
        fromAccount.setMoneyAmount(fromAccount.getMoneyAmount() - amount);
        toAccount.setMoneyAmount(toAccount.getMoneyAmount() + received);
        return new TransferResult(amount, commission, received);
    }

    public CloseResult close(int accountId) {
        Account accountToClose = getAccountOrThrow(accountId);
        User user = userService.findById(accountToClose.getUserId())
            .orElseThrow(() -> new BankException("user with id=%d not found".formatted(accountToClose.getUserId())));
        List<Account> userAccounts = user.getAccountList();
        Account targetAccount = userAccounts.stream()
            .filter(account -> account.getId() != accountId)
            .min(Comparator.comparing(Account::getId))
            .orElseThrow(() -> new BankException("cannot close account id=%d: it is the only account of user id=%d"
                .formatted(accountToClose.getId(), user.getId())));
        int transferred = accountToClose.getMoneyAmount();
        targetAccount.setMoneyAmount(targetAccount.getMoneyAmount() + transferred);
        userAccounts.remove(accountToClose);
        accounts.remove(accountToClose.getId());
        return new CloseResult(accountToClose.getId(), targetAccount.getId(), transferred);
    }

    private Account getAccountOrThrow(int accountId) {
        Account account = accounts.get(accountId);
        if (account == null) {
            throw new BankException("account with id=%d not found".formatted(accountId));
        }
        return account;
    }

    private void validateAmount(int amount) {
        if (amount <= 0) {
            throw new BankException("amount must be greater than zero");
        }
    }
}
