package org.pshenko.springcore.user;

import org.pshenko.springcore.exception.BankException;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class UserService {
    private final Map<Integer, User> users;
    private int idCounter;

    public UserService() {
        users = new HashMap<>();
        idCounter = 0;
    }

    public User create(String rawLogin) {
        String login = validateAndTrim(rawLogin);
        findByLogin(login).ifPresent(user -> {
            throw new BankException("user with login \"%s\" already exists".formatted(login));
        });
        idCounter++;
        User user = new User(idCounter, login, new ArrayList<>());
        users.put(idCounter, user);
        return user;
    }

    public Optional<User> findByLogin(String rawLogin) {
        String login = validateAndTrim(rawLogin);
        for (User user : users.values()) {
            if (user.getLogin().equals(login)) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    public Optional<User> findById(int id) {
        return Optional.ofNullable(users.get(id));
    }

    public List<User> findAll() {
        return users.values().stream().toList();
    }

    private String validateAndTrim(String login) {
        if (login == null || login.isBlank()) {
            throw new BankException("login cannot be empty");
        }
        return login.trim();
    }
}
