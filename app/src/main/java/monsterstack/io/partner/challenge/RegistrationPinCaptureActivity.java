package monsterstack.io.partner.challenge;

import android.content.Intent;

import monsterstack.io.api.UserSessionManager;
import monsterstack.io.partner.MainActivity;
import monsterstack.io.partner.R;

public class RegistrationPinCaptureActivity extends PinCaptureActivity {
    @Override
    public int getActionTitle() {
        return R.string.create_pin;
    }

    public void onNext() {
        UserSessionManager sessionManager = new UserSessionManager(this);
        String capturedPin = getCapturedPin();

        if (null != capturedPin) {
            sessionManager.createUserPin(capturedPin);
            Intent intent = new Intent(RegistrationPinCaptureActivity.this,
                    MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("source", MainActivity.class.getCanonicalName());
            startActivity(intent, enterStageRightBundle());
        } else {
            // Need to set a value and need an existing pin.
            showError(getResources().getString(getActionTitle()),
                    "Sign In Failed. Incorrect PIN!");
        }
    }
}
