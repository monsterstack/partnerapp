package monsterstack.io.api.resources;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Friend {
    private String id;

    @JsonProperty("user_id")
    private String userId;
}
