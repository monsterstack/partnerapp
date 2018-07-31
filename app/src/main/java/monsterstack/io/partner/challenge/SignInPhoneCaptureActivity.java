package monsterstack.io.partner.challenge;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import monsterstack.io.api.custom.AuthServiceCustom;
import monsterstack.io.api.listeners.OnResponseListener;
import monsterstack.io.api.resources.Challenge;
import monsterstack.io.api.resources.HttpError;
import monsterstack.io.partner.Application;
import monsterstack.io.partner.common.BasicActivity;

public class SignInPhoneCaptureActivity extends PhoneCaptureActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void injectDependencies(BasicActivity basicActivity) {
        super.injectDependencies(basicActivity);
        ((Application) getApplication()).component().inject(this);
    }

    @Override
    public void onCapture() {
        // Where can I get this from??
        if (!isValidPhoneNumber(this.presenter.getPhoneNumber(), "US")) {
            presenter.showError("Phone number is required to proceed");
        } else {
            presenter.showProgressBar();

            Challenge challenge = new Challenge();
            challenge.setPhoneNumber(presenter.getPhoneNumber());
            AuthServiceCustom authService = getServiceLocator().getAuthService();
            authService.requestAccess(challenge, onResponseListener());
        }
    }

    private OnResponseListener<Void, HttpError> onResponseListener() {
        return (aVoid, httpError) -> {
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
        };
    }
}
