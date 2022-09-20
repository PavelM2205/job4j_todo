package ru.job4j.repository;

import lombok.AllArgsConstructor;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.model.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class UserRepository {
    private static final Logger LOG = LoggerFactory.getLogger(UserRepository.class);
    private static final String FIND_BY_ID = "FROM User WHERE id = :fId";
    private static final String FIND_ALL = "FROM User";
    private static final String FIND_BY_LOGIN_AND_PASSWORD =
            "FROM User WHERE login = :fLogin AND password = :fPassword";
    private final CrudRepository crudRepository;

    public Optional<User> addUser(User user) {
        Optional<User> result = Optional.empty();
        try {
            crudRepository.run(session -> session.persist(user));
            result = Optional.of(user);
        } catch (Exception exc) {
            LOG.error("Exception when add User into DB: ", exc);
        }
        return result;
    }

    public Optional<User> findById(int id) {
        return crudRepository.optional(FIND_BY_ID, User.class, Map.of("fId", id));
    }

    public List<User> findAll() {
        return crudRepository.query(FIND_ALL, User.class);
    }

    public void update(User user) {
        crudRepository.run(session -> session.update(user));
    }

    public void delete(User user) {
        crudRepository.run(session -> session.remove(user));
    }

    public Optional<User> findByLoginAndPassword(String login, String password) {
        return crudRepository.optional(FIND_BY_LOGIN_AND_PASSWORD, User.class,
                Map.of("fLogin", login, "fPassword", password));
    }
}
