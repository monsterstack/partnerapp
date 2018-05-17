package monsterstack.io.api;

import java.util.List;

import monsterstack.io.api.resources.Identifier;
import monsterstack.io.api.resources.Member;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MemberService {
    @Headers({
            "Accept: application/json"
    })
    @GET("/partners/{partnerId}/members")
    Call<List<Member>> getMembers(@Path("partnerId")String partnerId,
                                  @Query("startAt")String transactionId,
                                  @Query("limit")Integer limit);

    @Headers({
            "Accept: application/json"
    })
    @GET("/partners/{partnerId}/members/{memberId}")
    Call<Member> getMember(@Path("partnerId")String partnerId, @Path("memberId") String memberId);


    @Headers({
            "Accept: application/json"
    })
    @POST("/partners/{partnerId}/members")
    Call<Identifier> createMember(@Path("partnerId")String partnerId);

    @Headers({
            "Accept: application/json"
    })
    @DELETE("/partners/{partnerId}/members/{memberId}")
    Call<Void> deleteMember(@Path("partnerId")String partnerId, @Path("memberId")String memberId);
}
