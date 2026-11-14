package org.pshenko.springcore;

import org.pshenko.springcore.config.ApplicationConfiguration;
import org.pshenko.springcore.user.User;
import org.pshenko.springcore.user.UserService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
            new AnnotationConfigApplicationContext(ApplicationConfiguration.class);
        UserService userService = context.getBean(UserService.class);
        userService.createUser("Artyom");
//        userService.createUser("Artyom");
        List<User> users = userService.findAll();
        users.forEach(System.out::println);
        userService.findById(1).ifPresentOrElse(
            System.out::println,
            () -> System.out.println("User not found")
        );
    }
}
