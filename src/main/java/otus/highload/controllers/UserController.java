package otus.highload.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import otus.highload.domain.User;
import otus.highload.dto.UserInfoDto;
import otus.highload.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/userInfo")
    //Id or Login???
    public String userInfo(@RequestParam("id") Integer id, Model model) {
        Optional<User> user = userService.findById(id);

        if (user.isPresent()) {
            model.addAttribute("USER", mapToUserInfo(user.get()));
            return "userInfo";
        }

        return "error";
    }

    @GetMapping("/userList")
    public String userList(Model model) {
        List<User> users = userService.findAll();

        List<UserInfoDto> dtos = users.stream().map(this::mapToUserInfo).collect(Collectors.toList());

        model.addAttribute("USERS", dtos);

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
