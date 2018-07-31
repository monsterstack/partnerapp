package monsterstack.io.partner.challenge;

import android.content.Intent;

import monsterstack.io.api.UserSessionManager;
import monsterstack.io.partner.settings.PinSettingsActivity;

public class ChangePinCaptureActivity extends PinCaptureActivity {
    @Override
    public void onNext() {
        if(!isValidPin(presenter.getEnteredPin())) {
            presenter.showError("Pin required to proceed");
        } else {
            UserSessionManager sessionManager = new UserSessionManager(this);
            String pin = sessionManager.getUserPin();
            String capturedPin = getCapturedPin();

            if (null != pin && capturedPin.equals(pin)) {
                Intent intent = new Intent(ChangePinCaptureActivity.this,
                        PinSettingsActivity.class);
                intent.putExtra("source", ChangePinCaptureActivity.class.getCanonicalName());
                startActivity(intent, enterStageRightBundle());
            } else {
                // Need to set a value and need an existing pin.
                showError(getResources().getString(getActionTitle()),
                        "Sign In Failed. Incorrect PIN!");
            }
        }
    }
}
