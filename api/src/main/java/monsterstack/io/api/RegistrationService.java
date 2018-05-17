package monsterstack.io.api;

import monsterstack.io.api.resources.Registration;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface RegistrationService {
    @Headers({
            "Accept: application/json"
    })
    @POST("/registrations")
    Call<Registration> register(@Body Registration registration);
}
