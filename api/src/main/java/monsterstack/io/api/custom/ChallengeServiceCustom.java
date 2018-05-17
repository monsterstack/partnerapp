package monsterstack.io.api.custom;

import monsterstack.io.api.ChallengeService;
import monsterstack.io.api.listeners.OnResponseListener;
import monsterstack.io.api.resources.Challenge;
import monsterstack.io.api.resources.HttpError;

public interface ChallengeServiceCustom extends ChallengeService {
    void submitChallenge(Challenge challenge, OnResponseListener<Void, HttpError> listener);
    void verifyChallengeCode(String code, OnResponseListener<Challenge, HttpError> listener);
}
