package otus.highload.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import otus.highload.domain.User;
import otus.highload.repository.UserRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Optional<User> registerUser(final User userToRegister) {

        try {
            return Optional.ofNullable(userRepository.create(userToRegister));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return Optional.empty();
    }

    public Optional<User> findById(Integer id) {
        return userRepository.findById(id);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }
}
