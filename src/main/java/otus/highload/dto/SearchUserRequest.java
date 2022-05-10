package otus.highload.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class SearchUserRequest {
    @NotBlank
    private String firstNamePattern;

    @NotBlank
    private String lastNamePattern;

    private Integer offset;

    private Integer limit;
}
