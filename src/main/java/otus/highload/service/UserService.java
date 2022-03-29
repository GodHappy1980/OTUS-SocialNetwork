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
            User user = userRepository.create(userToRegister);
            userRepository.grantRole(user.getId(), "ROLE_USER");
            user.setRoles(userRepository.findRolesByUserId(user.getId()));
            return Optional.ofNullable(user);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return Optional.empty();
    }

    public Optional<User> findById(Integer id) {
        Optional<User> user = userRepository.findById(id);
        user.ifPresent(u -> u.setRoles(userRepository.findRolesByUserId(u.getId())));
        return user;
    }

    public Optional<User> findByUserName(String userName) {
        Optional<User> res = userRepository.findByLogin(userName);
        res.ifPresent(u -> u.setRoles(userRepository.findRolesByUserId(u.getId())));
        return res;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public boolean updateUser(User user) {
        return userRepository.update(user);
    }
}
