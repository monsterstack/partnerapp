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
import monsterstack.io.partner.common.BasicActivity;
import monsterstack.io.partner.registration.control.RegistrationNameCaptureControl;
import monsterstack.io.partner.registration.presenter.RegistrationNameCapturePresenter;
import monsterstack.io.partner.utils.NavigationUtils;

public class RegistrationNameCaptureActivity extends BasicActivity implements RegistrationNameCaptureControl {

    private RegistrationNameCapturePresenter presenter;

    @Override
    public int getContentView() {
        return R.layout.registration_name_capture;
    }

    @Override
    public AppCompatActivity getActivity() {
        return this;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.presenter = getPresenterFactory().getRegistrationNameCapturePresenter(this, this);

        presenter.present(Optional.empty());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return presenter.onCreateOptionsMenu(menu);
    }

    @Override
    public int getActionTitle() {
        return R.string.registration;
    }

    @Override
    public void onNext(MenuItem menuItem){
        if (presenter.getFirstName() == null || presenter.getFirstName().isEmpty()) {
            presenter.showError("First name is required to proceed");
        } else if(presenter.getLastName() == null || presenter.getLastName().isEmpty()) {
            presenter.showError("Last name is required to proceed");
        } else {
            // Do the transition.
            presenter.showProgressBar();

            final Registration registration = presenter.buildRegistrationFromBindingData();

            Bundle bundle = NavigationUtils.enterStageRightBundle(getApplicationContext());
            Intent intent = new Intent(RegistrationNameCaptureActivity.this, RegistrationEmailCaptureActivity.class);
            intent.putExtra(RegistrationEmailCaptureActivity.REGISTRATION_EXTRA, registration);
            startActivity(intent, bundle);
            presenter.hideProgressBar();
        }
    }

    @Override
    public Context getContext() {
        return this;
    }
}
