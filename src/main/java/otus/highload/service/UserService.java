package otus.highload.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import otus.highload.domain.User;
import otus.highload.dto.UserInfoDto;
import otus.highload.repository.FriendshipRepository;
import otus.highload.repository.UserRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    private final FriendshipRepository friendshipRepository;

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

    public Optional<UserInfoDto> findById(User currentUser, Integer id) {
        UserInfoDto res = null;
        Optional<User> user = userRepository.findById(id);
//        user.ifPresent(u -> u.setRoles(userRepository.findRolesByUserId(u.getId())));

        if (user.isPresent()) {
            User u = user.get();
            u.setRoles(userRepository.findRolesByUserId(u.getId()));
            res = toUserInfoDto(u);
            res.setFriend(
                    friendshipRepository.findBySourceUserId(currentUser.getId())
                            .stream()
                            .map(f -> f.getDstUserId())
                            .collect(Collectors.toSet())
                            .contains(u.getId())
            );
        }

        return Optional.ofNullable(res);
    }

    public Optional<User> findByUserName(String userName) {
        Optional<User> res = userRepository.findByLogin(userName);
        res.ifPresent(u -> u.setRoles(userRepository.findRolesByUserId(u.getId())));
        return res;
    }

    public List<UserInfoDto> findAll(Optional<User> user) {
        List<UserInfoDto> res = userRepository.findAll().stream().map(this::toUserInfoDto).collect(Collectors.toList());

        user.ifPresent(u -> {
            Set<Integer> friendsUserIds = friendshipRepository.findBySourceUserId(u.getId()).stream().map(f -> f.getDstUserId()).collect(Collectors.toSet());
            res.stream().forEach(uid -> uid.setFriend(friendsUserIds.contains(uid.getId())));
        });

        return res;
    }

    //TODO: mapper
    private UserInfoDto toUserInfoDto(User user) {
        return UserInfoDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .gender(user.getGender())
                .interests(user.getInterests())
                .city(user.getCity())
                .build();
    }

    //TODO: mapper
    private UserInfoDto toUserInfoDto(User user, Boolean isFriend) {
        return UserInfoDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .gender(user.getGender())
                .interests(user.getInterests())
                .city(user.getCity())
                .friend(isFriend)
                .build();
    }

    public boolean updateUser(User user) {
        return userRepository.update(user);
    }

    public List<UserInfoDto> findFriends(User user) {
        List<User> friends = userRepository.findFriendsById(user.getId());
        return friends.stream().map(f -> toUserInfoDto(f, Boolean.TRUE)).collect(Collectors.toList());
    }
}
