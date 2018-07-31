package monsterstack.io.partner.challenge;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import javax.inject.Inject;

import monsterstack.io.api.UserSessionManager;
import monsterstack.io.partner.Application;
import monsterstack.io.partner.MainActivity;
import monsterstack.io.partner.R;
import monsterstack.io.partner.common.BasicActivity;
import monsterstack.io.partner.services.PermissionsService;

public class RegistrationPinCaptureActivity extends PinCaptureActivity {

    @Inject
    PermissionsService permissionsService;

    private transient Intent intent;

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
    public int getActionTitle() {
        return R.string.create_pin;
    }

    public void onNext() {
        if(!isValidPin(presenter.getEnteredPin())) {
            presenter.showError("Pin required to proceed");
        } else {
            UserSessionManager sessionManager = new UserSessionManager(this);
            String capturedPin = getCapturedPin();

            if (null != capturedPin) {
                sessionManager.createUserPin(capturedPin);

                intent = new Intent(RegistrationPinCaptureActivity.this,
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
}
