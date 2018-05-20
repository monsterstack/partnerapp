package monsterstack.io.partner.challenge;

import android.content.Intent;
import android.view.View;

import monsterstack.io.api.custom.ChallengeServiceCustom;
import monsterstack.io.api.listeners.OnResponseListener;
import monsterstack.io.api.resources.Challenge;
import monsterstack.io.api.resources.HttpError;

import static android.view.View.GONE;

public class RegistrationPhoneCaptureActivity extends PhoneCaptureActivity {

    @Override
    public void onCapture() {
        progressBar.setVisibility(View.VISIBLE);

        Challenge challenge = new Challenge();
        challenge.setPhoneNumber(editText.getText().toString());
        ChallengeServiceCustom challengeService = getServiceLocator().getChallengeService();

        challengeService.submitChallenge(challenge, onResponseListener());
    }

    private OnResponseListener<Void, HttpError> onResponseListener() {
        return new OnResponseListener<Void, HttpError>() {
            @Override
            public void onResponse(Void aVoid, HttpError httpError) {
                if (null == aVoid && null == httpError) {
                    Intent intent = new Intent(RegistrationPhoneCaptureActivity.this,
                            RegistrationChallengeVerificationActivity.class);
                    /* Copy all extras */
                    intent.putExtras(getIntent().getExtras());
                    applySourceToIntent(intent, PhoneCaptureActivity.class);

                    startActivity(intent, enterStageRightBundle());
                    progressBar.setVisibility(GONE);

                } else {
                    showHttpError(getResources().getString(getActionTitle()), httpError);
                    progressBar.setVisibility(GONE);
                }
            }
        };
    }
}
