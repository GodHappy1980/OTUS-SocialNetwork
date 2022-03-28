package otus.highload.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
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

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class RegisterUserController {
    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

//    @Resource(name="authenticationManager")
//    private final AuthenticationManager authManager;

    @GetMapping("/register")
    public String register() {
        return "registerNewUser";
    }

    @PostMapping("/register")
    public String registerUser(final RegisterUserRequest registerUserRequest, final HttpServletRequest request) {
        Optional<User> user = userService.registerUser(mapToUser(registerUserRequest));
        if (user.isPresent()) {
            UserPrincipal principal = new UserPrincipal(user.get());
            Authentication auth = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
//            authManager.authenticate(auth);
            SecurityContextHolder.getContext().setAuthentication(auth);
//            HttpSession session = request.getSession(true);
//            session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

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
