package otus.highload.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import otus.highload.domain.User;
import otus.highload.domain.UserPrincipal;
import otus.highload.dto.RegisterUserRequest;
import otus.highload.dto.UserInfoDto;
import otus.highload.service.UserService;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class RegisterUserController {
    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String register() {
        return "registerNewUser";
    }

    @PostMapping("/register")
    public String registerUser(final RegisterUserRequest registerUserRequest) {
        Optional<User> user = userService.registerUser(mapToUser(registerUserRequest));
        if (user.isPresent()) {
            UserPrincipal principal = new UserPrincipal(user.get());
            Authentication auth = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);

            return "redirect:/userList";
        }
        else return "error";
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

    //TODO: mapper
    private User mapToUser(RegisterUserRequest registerUserRequest) {
        return User.builder()
                .firstName(registerUserRequest.getFirstName())
                .lastName(registerUserRequest.getLastName())
                .gender(registerUserRequest.getGender())
                .interests(registerUserRequest.getInterests())
                .login(registerUserRequest.getLogin())
                .password(passwordEncoder.encode(registerUserRequest.getPassword()))
                .city(registerUserRequest.getCity())
                .enabled(true)
                .build();
    }
}
