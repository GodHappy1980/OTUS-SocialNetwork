package otus.highload.domain;

//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collection;
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
    private Integer id;

    private String firstName;

    private String lastName;

    private String gender;

    //либо аттрибут User - строка, либо отдельная табличка
    //private List<String> interests;
    //по простому - пусть будет простая строка
    private String interests;

    private String city;

    private String login;

    //Надо бы ignore
    private String password;

    private boolean enabled;

    private Collection<Role> roles;
}
