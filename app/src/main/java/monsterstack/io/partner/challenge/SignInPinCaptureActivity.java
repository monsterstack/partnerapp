package monsterstack.io.partner.challenge;

import android.content.Intent;

import com.google.firebase.auth.FirebaseAuth;

import monsterstack.io.api.UserSessionManager;
import monsterstack.io.api.resources.AuthenticatedUser;
import monsterstack.io.partner.MainActivity;
import monsterstack.io.partner.R;

public class SignInPinCaptureActivity extends PinCaptureActivity {

    @Override
    public int getActionTitle() {
        return R.string.sign_in;
    }

    @Override
    public void onNext() {
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

//                    final Context context = getApplicationContext();
//                    AsyncTask.execute(() -> MessagingService.initialize(context));
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
