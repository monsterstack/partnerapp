package monsterstack.io.partner.registration;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Map;
import java.util.Optional;

import butterknife.ButterKnife;
import monsterstack.io.api.resources.Registration;
import monsterstack.io.partner.R;
import monsterstack.io.partner.challenge.RegistrationPhoneCaptureActivity;
import monsterstack.io.partner.common.BasicActivity;
import monsterstack.io.partner.registration.control.RegistrationControl;
import monsterstack.io.partner.registration.presenter.RegistrationPresenter;
import monsterstack.io.partner.utils.NavigationUtils;

public class RegistrationActivity extends BasicActivity implements RegistrationControl {

    private RegistrationPresenter presenter;

    @Override
    public int getContentView() {
        return R.layout.registration;
    }

    @Override
    public AppCompatActivity getActivity() {
        return this;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new RegistrationPresenter(this);
        ButterKnife.bind(presenter, this);

        presenter.present(Optional.<Map>empty());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return presenter.onCreateOptionsMenu(menu);
    }

    @Override
    public int getActionTitle() {
        return R.string.registration;
    }

    public void onNext(MenuItem menuItem){
        presenter.showProgressBar();

        final Registration registration = presenter.buildRegistrationFromBindingData();

        Bundle bundle = NavigationUtils.enterStageRightBundle(getApplicationContext());
        Intent intent = new Intent(RegistrationActivity.this, RegistrationPhoneCaptureActivity.class);
        intent.putExtra(RegistrationPhoneCaptureActivity.REGISTRATION_EXTRA, registration);
        startActivity(intent, bundle);
        presenter.hideProgressBar();
    }

    @Override
    public Context getContext() {
        return this;
    }
}
