package otus.highload.dto;

import lombok.Data;

//import java.util.List;

@Data
public class RegisterUserRequest {
    private String firstName;

    private String lastName;

    private String gender;

    //private List<String> interests;
    private String interests;

    private String city;

    private String login;

    private String password;
}
