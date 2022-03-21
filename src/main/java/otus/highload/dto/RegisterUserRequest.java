package otus.highload.dto;

import lombok.Data;

import java.util.List;

@Data
public class RegisterUserRequest {
    String firstName;

    String lastName;

    String gender;

    //List<String> interests;
    String interests;

    String city;

    String login;

    String password;
}
