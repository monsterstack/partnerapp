package monsterstack.io.partner.challenge;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import monsterstack.io.api.UserSessionManager;
import monsterstack.io.api.resources.AuthenticatedUser;
import monsterstack.io.partner.MainActivity;
import monsterstack.io.partner.R;
import monsterstack.io.partner.services.MessagingService;

public class SignInPinCaptureActivity extends PinCaptureActivity {
    private static final String TAG = "SIGN_IN_PIN_CAPTURE";

    @BindView(R.id.progressbar)
    ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);
    }

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
            progressBar.setVisibility(View.VISIBLE);
            final AuthenticatedUser user = sessionManager.getUserDetails();
            Class destination = MainActivity.class;
            if (user.getTwoFactorAuth()) {
              destination = SignInPhoneCaptureActivity.class;
            } else {
                if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                    destination = SignInPhoneCaptureActivity.class;
                } else {
                    sessionManager.createUserSession(user);

                    final Context context = getApplicationContext();
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            MessagingService.initialize(context);
                        }
                    });
                }
            }

            Intent intent = new Intent(SignInPinCaptureActivity.this,
                    destination);
            intent.putExtra("source", destination.getCanonicalName());
            startActivity(intent, enterStageRightBundle());
            progressBar.setVisibility(View.GONE);
        } else {
            // Need to set a value and need an existing pin.
            showError(getResources().getString(getActionTitle()),
                    "Sign In Failed. Incorrect PIN!");
        }
    }
}
