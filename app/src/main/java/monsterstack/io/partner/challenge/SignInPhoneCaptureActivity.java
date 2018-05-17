package monsterstack.io.partner.challenge;

import android.content.Intent;
import android.view.View;

import butterknife.OnClick;
import monsterstack.io.api.custom.AuthServiceCustom;
import monsterstack.io.api.listeners.OnResponseListener;
import monsterstack.io.api.resources.Challenge;
import monsterstack.io.api.resources.HttpError;
import monsterstack.io.partner.R;

import static android.view.View.GONE;

public class SignInPhoneCaptureActivity extends PhoneCaptureActivity {

    @Override
    @OnClick(R.id.phoneCaptureButton)
    public void onCapture(View view) {
        progressBar.setVisibility(View.VISIBLE);

        Challenge challenge = new Challenge();
        challenge.setPhoneNumber(editText.getText().toString());
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
                    progressBar.setVisibility(GONE);

                } else {
                    showHttpError(getResources().getString(getActionTitle()), httpError);
                    progressBar.setVisibility(GONE);
                }
            }
        };
    }
}
