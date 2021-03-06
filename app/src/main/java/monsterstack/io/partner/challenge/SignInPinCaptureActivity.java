package monsterstack.io.partner.challenge;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Inject;

import monsterstack.io.api.UserSessionManager;
import monsterstack.io.api.resources.AuthenticatedUser;
import monsterstack.io.partner.Application;
import monsterstack.io.partner.MainActivity;
import monsterstack.io.partner.R;
import monsterstack.io.partner.common.BasicActivity;
import monsterstack.io.partner.services.PermissionsService;

public class SignInPinCaptureActivity extends PinCaptureActivity {

    @Inject
    PermissionsService permissionsService;

    @Override
    public int getActionTitle() {
        return R.string.sign_in;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Wish we can make all requests in bulk instead of piece-meal.
        // Also, where is the best place to make these requests.
        permissionsService.requestContactsPermissions(this);
    }

    @Override
    public void injectDependencies(BasicActivity basicActivity) {
        super.injectDependencies(basicActivity);
        ((Application) getApplication()).component().inject(this);
    }


    @Override
    public void onNext() {
        if(!isValidPin(presenter.getEnteredPin())) {
            presenter.showError("Pin required to proceed");
        } else {
            final UserSessionManager sessionManager = new UserSessionManager(this);
            String pin = sessionManager.getUserPin();
            String capturedPin = getCapturedPin();

            if (null != pin && capturedPin.equals(pin)) {
                presenter.showProgressBar();
                final AuthenticatedUser user = sessionManager.getUserDetails();
                Class destination = MainActivity.class;
                if (user.getTwoFactorAuth()) {
                    destination = SignInPhoneCaptureActivity.class;
                } else {
                    if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                        destination = SignInPhoneCaptureActivity.class;
                    } else {
                        sessionManager.createUserSession(user);
                    }
                }

                Intent intent = new Intent(SignInPinCaptureActivity.this,
                        destination);
                intent.putExtra("source", destination.getCanonicalName());
                startActivity(intent, enterStageRightBundle());
                presenter.hideProgressBar();
            } else {
                // Need to set a value and need an existing pin.
                showError(getResources().getString(getActionTitle()),
                        "Sign In Failed. Incorrect PIN!");
            }
        }
    }
}
