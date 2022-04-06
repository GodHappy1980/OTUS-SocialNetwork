package otus.highload.controllers;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import otus.highload.domain.User;
import otus.highload.dto.UserInfoDto;
import otus.highload.service.FriendshipService;
import otus.highload.service.UserService;
import otus.highload.util.SecurityHelper;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    private final FriendshipService friendshipService;

    @GetMapping("/userInfo")
    //Id or Login???
    public String userInfo(@RequestParam("id") Integer id, Model model) {
        Optional<User> currentUser = userService.findByUserName(SecurityHelper.getAuthenticatedUsername());

        if (currentUser.isPresent()) {
            Optional<UserInfoDto> user = userService.findById(currentUser.get(), id);

            if (user.isPresent()) {
                if (user.get().getId().equals(currentUser.get().getId())) {
                    model.addAttribute("currentUser", Boolean.TRUE);
                } else {
                    model.addAttribute("currentUser", Boolean.FALSE);
                }

                model.addAttribute("user", user.get());
                return "userInfo";
            } else {
                model.addAttribute("errors", new String[]{"Пользователь не найден"});
            }
        } else {
            model.addAttribute("errors", new String[]{"Пользователь не найден"});
        }

        return "error";
    }

    @GetMapping("/userList")
    public String userList(Model model) {
        List<UserInfoDto> users = userService.findAll(userService.findByUserName(SecurityHelper.getAuthenticatedUsername()));

        model.addAttribute("USERS", users);

        return "userList";
    }


    //TODO: mapper
    private UserInfoDto mapToUserInfo(User user) {
        return UserInfoDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .gender(user.getGender())
                .interests(user.getInterests())
                .city(user.getCity())
                .build();
    }
}
