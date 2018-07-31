package monsterstack.io.partner.challenge;

import android.content.Intent;

import monsterstack.io.api.custom.ChallengeServiceCustom;
import monsterstack.io.api.listeners.OnResponseListener;
import monsterstack.io.api.resources.Challenge;
import monsterstack.io.api.resources.HttpError;

public class RegistrationPhoneCaptureActivity extends PhoneCaptureActivity {
    public static final String REGISTRATION_EXTRA = "registration";

    @Override
    public void onCapture() {
        // Where??
        if (!isValidPhoneNumber(this.presenter.getPhoneNumber(), "US")) {
            presenter.showError("Phone number is required to proceed");
        } else {
            presenter.showProgressBar();

            Challenge challenge = new Challenge();
            challenge.setPhoneNumber(presenter.getPhoneNumber());
            ChallengeServiceCustom challengeService = getServiceLocator().getChallengeService();

            challengeService.submitChallenge(challenge, onResponseListener());
        }
    }

    private OnResponseListener<Void, HttpError> onResponseListener() {
        return (aVoid, httpError) -> {
            if (null == aVoid && null == httpError) {
                Intent intent = new Intent(RegistrationPhoneCaptureActivity.this,
                        RegistrationChallengeVerificationActivity.class);
                /* Copy all extras */
                intent.putExtras(getIntent().getExtras());
                applySourceToIntent(intent, PhoneCaptureActivity.class);

                startActivity(intent, enterStageRightBundle());
                presenter.hideProgressBar();

            } else {
                showHttpError(getResources().getString(getActionTitle()), httpError);
                presenter.showProgressBar();
            }
        };
    }
}
