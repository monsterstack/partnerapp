package monsterstack.io.partner.challenge;

import android.content.Intent;
import android.view.View;

import butterknife.OnClick;
import monsterstack.io.api.UserSessionManager;
import monsterstack.io.api.resources.User;
import monsterstack.io.partner.MainActivity;
import monsterstack.io.partner.R;

public class SignInPinCaptureActivity extends PinCaptureActivity {

    @Override
    public int getActionTitle() {
        return R.string.sign_in;
    }

    @OnClick(R.id.pinCaptureButton)
    public void onPinCapture(View view) {
        UserSessionManager sessionManager = new UserSessionManager(this);
        String pin = sessionManager.getUserPin();
        String capturedPin = getCapturedPin();

        if (null != pin && capturedPin.equals(pin)) {
            User user = sessionManager.getUserDetails();
            Class destination = MainActivity.class;
            if (user.getTwoFactorAuth()) {
              destination = SignInPhoneCaptureActivity.class;
            }

            Intent intent = new Intent(SignInPinCaptureActivity.this,
                    destination);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("source", destination.getCanonicalName());
            startActivity(intent, enterStageRightBundle());
        } else {
            // Need to set a value and need an existing pin.
            showError(getResources().getString(getActionTitle()),
                    "Sign In Failed. Incorrect PIN!");
        }
    }
}
