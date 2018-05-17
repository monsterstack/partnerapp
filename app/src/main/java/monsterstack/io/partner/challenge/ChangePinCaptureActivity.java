package monsterstack.io.partner.challenge;

import android.content.Intent;
import android.view.View;

import butterknife.OnClick;
import monsterstack.io.api.UserSessionManager;
import monsterstack.io.partner.R;
import monsterstack.io.partner.settings.PinSettingsActivity;

public class ChangePinCaptureActivity extends PinCaptureActivity {

    @OnClick(R.id.pinCaptureButton)
    public void onPinCapture(View view) {
        UserSessionManager sessionManager = new UserSessionManager(this);
        String pin = sessionManager.getUserPin();
        String capturedPin = getCapturedPin();

        if (null != pin && capturedPin.equals(pin)) {
            Intent intent = new Intent(ChangePinCaptureActivity.this,
                    PinSettingsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("source", ChangePinCaptureActivity.class.getCanonicalName());
            startActivity(intent, enterStageRightBundle());
        } else {
            // Need to set a value and need an existing pin.
            showError(getResources().getString(getActionTitle()),
                    "Sign In Failed. Incorrect PIN!");
        }
    }
}
