package org.pshenko.springcore.user;

import org.pshenko.springcore.exception.BankException;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class UserService {
    private final Map<Integer, User> users = new HashMap<>();
    private int nextId;

    public User createUser(String login) {
        if (login == null || login.isBlank()) {
            throw new BankException("Login cannot be empty");
        }
        Optional<User> existingUser = findByLogin(login);
        if (existingUser.isPresent()) {
            throw new BankException("User with login \"%s\" already exists".formatted(login));
        }
        nextId++;
        User user = new User(nextId, login, new ArrayList<>());
        users.put(nextId, user);
        return user;
    }

    public Optional<User> findByLogin(String login) {
        if (login == null || login.isBlank()) {
            throw new BankException("Login cannot be empty");
        }
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
}
