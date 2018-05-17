package monsterstack.io.api;

import java.util.List;

import monsterstack.io.api.resources.Identifier;
import monsterstack.io.api.resources.User;
import monsterstack.io.api.resources.Wallet;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserService {
    @Headers({
            "Accept: application/json"
    })
    @POST("/users")
    Call<Identifier> createUser(@Body User user);

    @Headers({
            "Accept: application/json"
    })
    @PUT("/users/{userId}")
    Call<Identifier> updateUser(@Path("userId") String userId, @Body User user);

    @Headers({
            "Accept: application/json"
    })
    @DELETE("/users/{userId}")
    Call<Void> deleteUser(@Path("userId")String userId);

    @Headers({
            "Accept: application/json"
    })
    @GET("/users/{userId}")
    Call<User> getUser(@Header("Authorization") String bearerToken, @Path("userId")String userId);

    @Headers({
            "Accept: application/json"
    })
    @GET("/users/{userId}")
    Call<User> getUser(@Path("userId")String userId);

    @Headers({
            "Accept: application/json"
    })
    @GET("/users")
    Call<List<User>> getUsers(@Query("startAt")String userId, @Query("limit")Integer limit);

    @Headers({
            "Accept: application/json"
    })
    @POST("/users/{userId}/wallet")
    Call<Identifier> attachWalletToUser(@Path("userId") String userId, @Body Wallet wallet);

    @Headers({
            "Accept: application/json"
    })
    @DELETE("/users/{userId}/wallet")
    Call<Void> detachWalletFromUser(@Path("userId") String userId);
}
