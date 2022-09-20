package ru.job4j.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.exception.UserWithSuchLoginAlreadyExists;
import ru.job4j.exception.UserWithSuchLoginAndPasswordDoesNotExists;
import ru.job4j.model.User;
import ru.job4j.repository.UserRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User addUser(User user) {
        Optional<User> optUser = userRepository.addUser(user);
        if (optUser.isEmpty()) {
            throw new UserWithSuchLoginAlreadyExists("User with such login already exists");
        }
        return optUser.get();
    }

    public User findById(int id) {
        Optional<User> optUser = userRepository.findById(id);
        if (optUser.isEmpty()) {
            throw new NoSuchElementException("User was not found");
        }
        return optUser.get();
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void update(User user) {
        userRepository.update(user);
    }

    public void delete(User user) {
        userRepository.delete(user);
    }

    public User findByLoginAndPassword(String login, String password) {
        Optional<User> optUser = userRepository.findByLoginAndPassword(login, password);
        if (optUser.isEmpty()) {
            throw new UserWithSuchLoginAndPasswordDoesNotExists(
                    "User with this login and password does not exist");
        }
        return optUser.get();
    }
}
