package monsterstack.io.api.resources;

import java.util.List;

import lombok.Data;

@Data
public class Invitation {
    private String userId;
    private String partnerId;

    private List<Friend> friends;
}
