package otus.highload.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import otus.highload.domain.User;
import otus.highload.dto.FriendshipForm;
import otus.highload.service.FriendshipService;
import otus.highload.service.UserService;
import otus.highload.util.SecurityHelper;

import javax.validation.Valid;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class FriendshipController {

    private final FriendshipService friendshipService;

    private final UserService userService;

    @PostMapping("/friends")
    public String process(
            @Valid FriendshipForm friendshipForm,
            Model model
    ) {
        Optional<User> currentUser = userService.findByUserName(SecurityHelper.getAuthenticatedUsername());
        List<String> errors = new LinkedList<>();

        if (currentUser.isPresent()) {
            Optional<User> friend = userService.findById(friendshipForm.getDstUserId());

            if (friend.isPresent()) {
                if ("REMOVE".equalsIgnoreCase(friendshipForm.getAction())) {
                    int affectedRowCount = friendshipService.removeFriend(currentUser.get(), friend.get());

                    if (affectedRowCount < 1) {
                        errors.add("Не удалось прекратить дружбу");
                    }
                } else if ("ADD".equalsIgnoreCase(friendshipForm.getAction())) {
                    if (friendshipService.addFriend(currentUser.get(), friend.get())) {
                        errors.add("Не удалось подружиться");
                    }
                } else {
                    errors.add("Неизвестная команда");
                }
            } else {
                errors.add("Пользователь не найден");
            }
        } else {
            errors.add("Пользователь не найден");
        }

        return "redirect:/userList";
    }

    @GetMapping("/friends")
    public String getFriends(Model model) {
        Optional<User> currentUser = userService.findByUserName(SecurityHelper.getAuthenticatedUsername());
        List<String> errors = new LinkedList<>();

        if (currentUser.isPresent()) {
            model.addAttribute("friends", userService.findFriends(currentUser.get()));
        } else {
            errors.add("Пользователь не найден");
        }

        return "/friends";
    }
}
