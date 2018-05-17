package monsterstack.io.api;

import java.util.List;

import monsterstack.io.api.resources.Friend;
import monsterstack.io.api.resources.Identifier;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface FriendService {
    @Headers({
            "Accept: application/json"
    })
    @POST("/users/{userId}/friends")
    Call<Identifier> createFriend(@Path("userId") String userId, @Body Friend friend);

    @Headers({
            "Accept: application/json"
    })
    @DELETE("/users/{userId}/friends/{friendId}")
    Call<Void> deleteFriend(@Path("userId") String userId, @Path("friendId") String friendId);

    @Headers({
            "Accept: application/json"
    })
    @GET("/users/{userId}/friends")
    Call<List<Friend>> getUserFriends(@Path("userId") String userId, @Query("startAt") String friendId, @Query("limit") Integer limit);

}
