package monsterstack.io.api.resources;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthVerification extends Challenge {

    @JsonProperty("signin_token")
    private String signInToken;

    private String idToken;
}
