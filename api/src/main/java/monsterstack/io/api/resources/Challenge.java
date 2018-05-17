package monsterstack.io.api.resources;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Challenge implements Serializable {
    private String id;
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private Date expiry;

    private String code;

    private Boolean valid;

    @JsonProperty("phone_number")
    private String phoneNumber;


    @JsonProperty("user_id")
    private String userId;
}
