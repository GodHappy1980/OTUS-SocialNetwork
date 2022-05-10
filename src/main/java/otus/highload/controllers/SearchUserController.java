package otus.highload.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import otus.highload.dto.SearchUserRequest;
import otus.highload.service.UserService;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class SearchUserController {
    private final UserService userService;

    @GetMapping("/search")
    public String searchPage() {
        return "userSearch";
    }

    @PostMapping("/search")
    public String search(
            final @Valid SearchUserRequest searchUserRequest,
            final Model model
    ) {
        if (searchUserRequest.getLimit() != null) {
            if (searchUserRequest.getOffset() != null) {
                model.addAttribute(
                        "USERS",
                        userService.findByFirstNameAndLastName(
                                searchUserRequest.getFirstNamePattern(),
                                searchUserRequest.getLastNamePattern(),
                                searchUserRequest.getLimit(),
                                searchUserRequest.getOffset()
                        )
                );
            } else {
                model.addAttribute(
                        "USERS",
                        userService.findByFirstNameAndLastName(
                                searchUserRequest.getFirstNamePattern(),
                                searchUserRequest.getLastNamePattern(),
                                searchUserRequest.getLimit()
                        )
                );
            }
        } else {
            model.addAttribute(
                    "USERS",
                    userService.findByFirstNameAndLastName(
                            searchUserRequest.getFirstNamePattern(),
                            searchUserRequest.getLastNamePattern()
                    )
            );
        }

        return "userList";
    }
}
