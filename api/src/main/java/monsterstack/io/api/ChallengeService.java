package monsterstack.io.api;

import monsterstack.io.api.resources.Challenge;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ChallengeService {
    @Headers({
            "Accept: application/json"
    })
    @POST("/challenges")
    Call<Void> submitChallenge(@Body Challenge challenge);

    @Headers({
            "Accept: application/json"
    })
    @GET("/challenges/{code}")
    Call<Challenge> verifyChallengeByCode(@Path("code") String code);
}
