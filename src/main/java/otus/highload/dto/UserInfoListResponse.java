package otus.highload.dto;

import lombok.Data;

import java.util.Collection;

@Data
public class UserInfoListResponse {
    Collection<UserInfoDto> users;
}
