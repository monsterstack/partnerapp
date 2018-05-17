package monsterstack.io.api.resources;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import okhttp3.ResponseBody;
import retrofit2.Response;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class HttpError {
    @JsonProperty("statusCode")
    private int statusCode;
    @JsonProperty("errorMessage")
    private String message;

    public HttpError(Response response) {
        ResponseBody responseBody = response.errorBody();

        try {
            String error = responseBody.string();
            HttpError realError = new ObjectMapper().readValue(error, HttpError.class);
            this.statusCode = realError.getStatusCode();
            this.message = realError.getMessage();
        } catch (IOException ioException) {
            throw new IllegalArgumentException("Invalid Error Body");
        }
    }
}
