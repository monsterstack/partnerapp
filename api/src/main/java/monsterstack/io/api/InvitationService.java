package monsterstack.io.api;

import java.util.List;

import monsterstack.io.api.resources.Invitation;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface InvitationService {
    @Headers({
            "Accept: application/json"
    })
    @GET("/invitations")
    Call<List<Invitation>> getPartnerInvitations(@Path("partnerId")String partnerId,
                                                 @Query("startAt")String invitationId,
                                                 @Query("limit")Integer limit);
}
