package monsterstack.io.partner;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import monsterstack.io.api.UserSessionManager;
import monsterstack.io.partner.challenge.SignInPhoneCaptureActivity;
import monsterstack.io.partner.challenge.SignInPinCaptureActivity;
import monsterstack.io.partner.registration.RegistrationActivity;
import monsterstack.io.partner.utils.NavigationUtils;

public class LaunchActivity extends AppCompatActivity {

    @BindView(R.id.signUpButton)
    Button signUpButton;
    @BindView(R.id.signInButton)
    Button signInButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launch);
        ButterKnife.bind(this);
    }


    @OnClick(R.id.signUpButton)
    public void onSignUp(View view) {
        Bundle bundle = enterStageRightBundle();

        Intent intent = new Intent(LaunchActivity.this, RegistrationActivity.class);
        startActivity(intent, bundle);
    }

    @OnClick(R.id.signInButton)
    public void onSignIn(View view) {
        UserSessionManager sessionManager = new UserSessionManager(this);
        String pin = sessionManager.getUserPin();

        if (null != pin) {
            onPinSignIn(view);
        } else {
            Bundle bundle = enterStageRightBundle();
            Intent intent = new Intent(LaunchActivity.this, SignInPhoneCaptureActivity.class);
            intent.putExtra("source", LaunchActivity.class.getCanonicalName());
            startActivity(intent, bundle);
        }
    }

    public void onPinSignIn(View view) {
        Bundle bundle = enterStageRightBundle();
        Intent intent = new Intent(LaunchActivity.this, SignInPinCaptureActivity.class);
        intent.putExtra("source", LaunchActivity.class.getCanonicalName());
        startActivity(intent, bundle);
    }

    private Bundle enterStageRightBundle() {
        return NavigationUtils.enterStageRightBundle(getApplicationContext());
    }
}
