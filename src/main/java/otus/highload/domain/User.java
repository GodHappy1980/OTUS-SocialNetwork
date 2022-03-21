package otus.highload.domain;

//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
//import java.util.List;

//@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    static final long serialVersionUID = 1L;

//    @Id
//    @GeneratedValue
    Integer id;

    String firstName;

    String lastName;

    String gender;

    //либо аттрибут User - строка, либо отдельная табличка
    //List<String> interests;
    //по простому - пусть будет простая строка
    String interests;

    String city;

    String login;

    //Надо бы ignore
    String password;
}
