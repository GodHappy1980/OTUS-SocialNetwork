package otus.highload.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FriendshipForm {
    Integer dstUserId;
    //Enum???
    String action;
}
