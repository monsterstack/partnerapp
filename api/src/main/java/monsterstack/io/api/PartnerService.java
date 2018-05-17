package monsterstack.io.api;

import java.util.List;

import monsterstack.io.api.resources.Identifier;
import monsterstack.io.api.resources.Partner;
import monsterstack.io.api.resources.Wallet;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PartnerService {
    @Headers({
            "Accept: application/json"
    })
    @POST("/partners")
    Call<Identifier> createPartner(@Body Partner partner);

    @Headers({
            "Accept: application/json"
    })
    @GET("/partners/{partnerId}")
    Call<Partner> getPartner(@Path("partnerId")String partnerId);


    @Headers({
            "Accept: application/json"
    })
    @GET("/partners")
    Call<List<Partner>> getPartners(@Query("startAt") String partnerId, @Query("limit") Integer limit);

    @Headers({
            "Accept: application/json"
    })
    @POST("/partners/{partnerId}/wallet")
    Call<Identifier> attachWalletToPartner(@Path("partnerId") String partnerId, @Body Wallet wallet);

    @Headers({
            "Accept: application/json"
    })
    @DELETE("/partners/{partnerId}/wallet")
    Call<Void> detachWalletFromPartner(@Path("partnerId") String partnerId);
}
