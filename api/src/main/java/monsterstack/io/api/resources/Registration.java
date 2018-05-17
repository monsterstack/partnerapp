package monsterstack.io.api.resources;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Registration implements Serializable {
    private MinimalUser user;

    @JsonProperty("signin_token")
    private String signInToken;

    @JsonProperty("user_id")
    private String userId;

    private String idToken;
}
