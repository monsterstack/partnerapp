package monsterstack.io.api;

import monsterstack.io.api.resources.AuthVerification;
import monsterstack.io.api.resources.Challenge;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface AuthService {
    @Headers({
            "Accept: application/json"
    })
    @POST("/auth")
    Call<Void> requestAccess(@Body Challenge challenge);

    @Headers({
            "Accept: application/json"
    })
    @GET("/auth/{code}")
    Call<AuthVerification> verifyAuthByCode(@Path("code") String code);
}
