package monsterstack.io.partner.registration;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Optional;

import monsterstack.io.api.resources.Registration;
import monsterstack.io.partner.R;
import monsterstack.io.partner.challenge.RegistrationPhoneCaptureActivity;
import monsterstack.io.partner.common.BasicActivity;
import monsterstack.io.partner.registration.control.RegistrationEmailCaptureControl;
import monsterstack.io.partner.registration.presenter.RegistrationEmailCapturePresenter;
import monsterstack.io.partner.utils.NavigationUtils;

public class RegistrationEmailCaptureActivity extends BasicActivity implements RegistrationEmailCaptureControl {
    public static final String REGISTRATION_EXTRA = "registration";
    private RegistrationEmailCapturePresenter presenter;

    private Registration incomingRegistration;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.presenter = getPresenterFactory().getRegistrationEmailCapturePresenter(this, this);

        presenter.present(Optional.empty());

        incomingRegistration = (Registration)getIntent().getSerializableExtra(REGISTRATION_EXTRA);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return presenter.onCreateOptionsMenu(menu);
    }


    @Override
    public int getContentView() {
        return R.layout.registration_email_capture;
    }

    @Override
    public AppCompatActivity getActivity() {
        return this;
    }

    @Override
    public int getActionTitle() {
        return R.string.detail_settings_email;
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onNext(MenuItem menuItem) {
        if (!isValidEmail(presenter.getEmailAddress())) {
            presenter.showError("Email Address is required to proceed");
        } else {
            presenter.showProgressBar();

            final Registration registration = presenter.buildRegistrationFromBindingData();
            registration.getUser().setFirstName(incomingRegistration.getUser().getFirstName());
            registration.getUser().setLastName(incomingRegistration.getUser().getLastName());

            Bundle bundle = NavigationUtils.enterStageRightBundle(getApplicationContext());
            Intent intent = new Intent(RegistrationEmailCaptureActivity.this, RegistrationPhoneCaptureActivity.class);
            intent.putExtra(RegistrationPhoneCaptureActivity.REGISTRATION_EXTRA, registration);
            startActivity(intent, bundle);
            presenter.hideProgressBar();
        }
    }
}
