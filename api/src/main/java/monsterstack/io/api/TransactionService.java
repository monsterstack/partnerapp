package monsterstack.io.api;

import java.util.List;

import monsterstack.io.api.resources.Balance;
import monsterstack.io.api.resources.Contribution;
import monsterstack.io.api.resources.Draw;
import monsterstack.io.api.resources.Identifier;
import monsterstack.io.api.resources.Transaction;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TransactionService {
    @Headers({
            "Accept: application/json"
    })
    @POST("/partners/{partnerId}/draw")
    Call<Identifier> createPartnerDraw(@Path("partnerId")String partnerId, @Body Draw draw);

    @Headers({
            "Accept: application/json"
    })
    @POST("/partners/{partnerId}/contribution")
    Call<Identifier> createPartnerContribution(@Path("partnerId")String partnerId, @Body Contribution contribution);

    @Headers({
            "Accept: application/json"
    })
    @GET("/partners/{partnerId}/balance")
    Call<Balance> getPartnerBalance(@Path("partnerId")String partnerId);

    @Headers({
            "Accept: application/json"
    })
    @GET("/partners/{partnerId}/transactions")
    Call<List<Transaction>> getPartnerTransactions(@Path("partnerId")String partnerId,
                                             @Query("startAt")String transactionId,
                                             @Query("limit")Integer limit);
}
