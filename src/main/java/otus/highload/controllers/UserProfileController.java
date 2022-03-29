package otus.highload.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import otus.highload.domain.User;
import otus.highload.domain.UserPrincipal;
import otus.highload.dto.UpdateUserProfileRequest;
import otus.highload.dto.UserInfoDto;
import otus.highload.service.UserDetailsServiceImpl;
import otus.highload.service.UserService;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class UserProfileController {
    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    @GetMapping("/profile")
    public String userProfile(Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getPrincipal() instanceof UserPrincipal) {
            UserPrincipal userPrincipal = (UserPrincipal)auth.getPrincipal();
            Optional<User> user = userService.findByUserName(userPrincipal.getUsername());

            if (user.isPresent()) {
                model.addAttribute("user", mapToUserInfo(user.get()));
                return "userProfile";
            }
        }

        return "error";
    }

    @PostMapping("/profile")
    public String modifyUserProfile(final UpdateUserProfileRequest updateUserProfileRequest, Model model) {
        Optional<User> user = userService.findById(updateUserProfileRequest.getId());

        if (user.isPresent()) {
            //TODO:check unique username, enabled, etc

            //TODO:update user
            User modifiedUser = mapToUser(updateUserProfileRequest);
            if(userService.updateUser(modifiedUser)) {
                UserPrincipal principal = new UserPrincipal(userService.findByUserName(modifiedUser.getLogin()).get());
                Authentication auth = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);

                model.addAttribute("user", mapToUserInfo(modifiedUser));
                //return "userProfile";
                return "userList";
            }
        }

        return "error";
    }

    private User mapToUser(final UpdateUserProfileRequest updateUserProfileRequest) {
        return User.builder()
                .id(updateUserProfileRequest.getId())
                .firstName(updateUserProfileRequest.getFirstName())
                .lastName(updateUserProfileRequest.getLastName())
                .gender(updateUserProfileRequest.getGender())
                .interests(updateUserProfileRequest.getInterests())
                .city(updateUserProfileRequest.getCity())
                .login(updateUserProfileRequest.getLogin())
                .password(passwordEncoder.encode(updateUserProfileRequest.getPassword()))
                .enabled(true)
                .build();
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
                .login(user.getLogin())
                .build();
    }

}
