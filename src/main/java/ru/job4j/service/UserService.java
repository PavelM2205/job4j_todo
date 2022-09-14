package ru.job4j.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.model.User;
import ru.job4j.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User addUser(User user) {
        return userRepository.addUser(user).get();
    }

    public Optional<User> findById(int id) {
        return userRepository.findById(id);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public boolean update(User user) {
        return userRepository.update(user);
    }

    public boolean delete(User user) {
        return userRepository.delete(user);
    }

    public User findByLoginAndPassword(String login, String password) {
        Optional<User> optUser = userRepository.findByLoginAndPassword(login, password);
        if (optUser.isEmpty()) {
            throw new IllegalArgumentException(
                    "User with this login and password does not exist");
        }
        return optUser.get();
    }
}
