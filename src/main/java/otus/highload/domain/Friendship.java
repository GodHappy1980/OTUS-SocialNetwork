package otus.highload.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Friendship implements Serializable {
    static final long serialVersionUID = 1L;

    private Integer id;

    private Integer srcUserId;

    private Integer dstUserId;

    private Byte status;//TODO enum
}
