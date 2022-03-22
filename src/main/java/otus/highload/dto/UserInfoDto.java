package otus.highload.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserInfoDto {
    Integer id;

    String firstName;

    String lastName;

    String gender;

    //либо аттрибут User - строка, либо отдельная табличка
    //List<String> interests;
    //по простому - пусть будет простая строка
    String interests;

    String city;
}
