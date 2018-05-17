package monsterstack.io.api.resources;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import lombok.Data;

@Data
public class FriendRequest {
    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("users")
    private List<UserIdentifier> users;
}
