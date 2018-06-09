package monsterstack.io.partner.challenge;

import android.content.Intent;

import monsterstack.io.api.custom.AuthServiceCustom;
import monsterstack.io.api.listeners.OnResponseListener;
import monsterstack.io.api.resources.Challenge;
import monsterstack.io.api.resources.HttpError;

public class SignInPhoneCaptureActivity extends PhoneCaptureActivity {

    @Override
    public void onCapture() {
        presenter.showProgressBar();

        Challenge challenge = new Challenge();
        challenge.setPhoneNumber(presenter.getPhoneNumber());
        AuthServiceCustom authService = getServiceLocator().getAuthService();
        authService.requestAccess(challenge, onResponseListener());
    }

    private OnResponseListener<Void, HttpError> onResponseListener() {
        return new OnResponseListener<Void, HttpError>() {
            @Override
            public void onResponse(Void aVoid, HttpError httpError) {
                if (null == aVoid && null == httpError) {
                    Intent intent = new Intent(SignInPhoneCaptureActivity.this,
                            SignInChallengeVerificationActivity.class);
                    /* Copy all extras */
                    intent.putExtras(getIntent().getExtras());
                    applySourceToIntent(intent, SignInPhoneCaptureActivity.class);

                    startActivity(intent, enterStageRightBundle());
                    presenter.hideProgressBar();

                } else {
                    showHttpError(getResources().getString(getActionTitle()), httpError);
                    presenter.hideProgressBar();
                }
            }
        };
    }
}
