package monsterstack.io.partner.challenge.control;

import android.text.TextUtils;

import monsterstack.io.partner.common.Control;

public interface ChallengeVerificationControl extends Control {
    void onVerify();
    default boolean isValidChallengeCode(String challengeCode) {
        return (!TextUtils.isEmpty(challengeCode) && TextUtils.isDigitsOnly(challengeCode)
                && challengeCode.length() == 4);
    }
}
