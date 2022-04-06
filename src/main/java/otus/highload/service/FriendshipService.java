package otus.highload.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import otus.highload.domain.Friendship;
import otus.highload.domain.User;
import otus.highload.repository.FriendshipRepository;


@Service
@RequiredArgsConstructor
public class FriendshipService {
    private final FriendshipRepository friendshipRepository;

    public int removeFriend(User currentUser, User friend) {
        return friendshipRepository.deleteFriendship(currentUser.getId(), friend.getId());
    }

    public boolean addFriend(User currentUser, User friend) {
        return friendshipRepository.insertFriendship(currentUser.getId(), friend.getId());
    }
}
