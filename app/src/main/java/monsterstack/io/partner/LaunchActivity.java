package monsterstack.io.partner;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.util.Optional;

import javax.inject.Inject;

import monsterstack.io.api.UserSessionManager;
import monsterstack.io.partner.challenge.SignInPhoneCaptureActivity;
import monsterstack.io.partner.challenge.SignInPinCaptureActivity;
import monsterstack.io.partner.control.LaunchControl;
import monsterstack.io.partner.presenter.LaunchPresenter;
import monsterstack.io.partner.registration.RegistrationNameCaptureActivity;
import monsterstack.io.partner.utils.NavigationUtils;

public class LaunchActivity extends AppCompatActivity implements LaunchControl {

    @Inject
    PresenterFactory presenterFactory;

    LaunchPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launch);

        ((Application)getApplication()).component().inject(this);

        presenter = (LaunchPresenter)presenterFactory.getLaunchPresenter(this, this)
                .present(Optional.empty());
    }

    @Override
    public Context getContext() {
        return this;
    }

    public void onSignUp() {
        Bundle bundle = enterStageRightBundle();

        Intent intent = new Intent(this, RegistrationNameCaptureActivity.class);
        startActivity(intent, bundle);
    }

    public void onSignIn() {
        UserSessionManager sessionManager = new UserSessionManager(this);
        String pin = sessionManager.getUserPin();

        if (null != pin) {
            onPinSignIn();
        } else {
            Bundle bundle = enterStageRightBundle();
            Intent intent = new Intent(this, SignInPhoneCaptureActivity.class);
            intent.putExtra("source", LaunchActivity.class.getCanonicalName());
            startActivity(intent, bundle);
        }
    }

    public void onPinSignIn() {
        Bundle bundle = enterStageRightBundle();
        Intent intent = new Intent(this, SignInPinCaptureActivity.class);
        intent.putExtra("source", LaunchActivity.class.getCanonicalName());
        startActivity(intent, bundle);
    }

    private Bundle enterStageRightBundle() {
        return NavigationUtils.enterStageRightBundle(this);
    }
}
