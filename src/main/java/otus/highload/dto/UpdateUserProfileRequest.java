package otus.highload.dto;

import lombok.Data;

@Data
public class UpdateUserProfileRequest {
    private Integer id;

    private String firstName;

    private String lastName;

    private String gender;

    private String interests;

    private String city;

    private String login;

    private String password;

}
