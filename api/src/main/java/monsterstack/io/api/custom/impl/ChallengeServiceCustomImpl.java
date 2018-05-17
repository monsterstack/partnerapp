package monsterstack.io.api.custom.impl;

import android.support.annotation.NonNull;

import monsterstack.io.api.ChallengeService;
import monsterstack.io.api.custom.ChallengeServiceCustom;
import monsterstack.io.api.listeners.OnResponseListener;
import monsterstack.io.api.resources.Challenge;
import monsterstack.io.api.resources.HttpError;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChallengeServiceCustomImpl implements ChallengeService, ChallengeServiceCustom {
    private ChallengeService challengeService;

    public ChallengeServiceCustomImpl(ChallengeService challengeService) {
        this.challengeService = challengeService;
    }

    @Override
    public Call<Void> submitChallenge(Challenge challenge) {
        return challengeService.submitChallenge(challenge);
    }

    @Override
    public Call<Challenge> verifyChallengeByCode(String code) {
        return challengeService.verifyChallengeByCode(code);
    }

    public void submitChallenge(Challenge challenge, final OnResponseListener<Void, HttpError> listener) {
        Call<Void> challengeCall = challengeService.submitChallenge(challenge);
        challengeCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    listener.onResponse(null, null);
                } else {
                    listener.onResponse(null, new HttpError(response.code(),
                            response.message()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                listener.onResponse(null, new HttpError(500, t.getMessage()));
            }
        });
    }

    public void verifyChallengeCode(String code, final OnResponseListener<Challenge, HttpError> listener) {
        Call<Challenge> challengeCall = challengeService.verifyChallengeByCode(code);
        challengeCall.enqueue(new Callback<Challenge>() {
            @Override
            public void onResponse(@NonNull Call<Challenge> call, @NonNull Response<Challenge> response) {
                if(response.isSuccessful()) {
                    listener.onResponse(response.body(), null);
                } else {
                    listener.onResponse(null, new HttpError(response.code(), response.message()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<Challenge> call, @NonNull Throwable t) {
                listener.onResponse(null, new HttpError(500, t.getMessage()));
            }
        });
    }
}
