package monsterstack.io.partner.registration.presenter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import java.util.Map;
import java.util.Optional;

import butterknife.BindView;
import monsterstack.io.api.resources.MinimalUser;
import monsterstack.io.api.resources.Registration;
import monsterstack.io.partner.R;
import monsterstack.io.partner.common.HasProgressBarSupport;
import monsterstack.io.partner.common.presenter.Presenter;
import monsterstack.io.partner.registration.RegistrationActivity;
import monsterstack.io.partner.registration.control.RegistrationControl;

public class RegistrationPresenter implements Presenter<RegistrationControl>, HasProgressBarSupport {
    @BindView(R.id.first_name)
    EditText firstNameEdit;

    @BindView(R.id.last_name)
    EditText lastNameEdit;

    @BindView(R.id.email_address)
    EditText emailEdit;

    @BindView(R.id.progressbar)
    ProgressBar progressBar;

    @BindView(R.id.my_toolbar)
    Toolbar toolbar;

    private Context context;

    private RegistrationControl control;

    public RegistrationPresenter(Context context) {
        this.context = context;
    }

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public Presenter<RegistrationControl> present(Optional<Map> metadata) {
        hideProgressBar();
        return this;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        ((Activity)context).getMenuInflater().inflate(R.menu.next_action, menu);

        menu.findItem(R.id.next_button).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                ((RegistrationActivity)context).onNext(item);
                return false;
            }
        });
        return true;
    }

    public Registration buildRegistrationFromBindingData() {
        String emailAddress = emailEdit.getText().toString();
        String firstName = firstNameEdit.getText().toString();
        String lastName = lastNameEdit.getText().toString();

        Registration registration = new Registration();
        MinimalUser minimalUser = new MinimalUser();
        minimalUser.setFirstName(firstName);
        minimalUser.setLastName(lastName);
        minimalUser.setEmailAddress(emailAddress);
        registration.setUser(minimalUser);

        return registration;
    }

    @Override
    public Presenter<RegistrationControl> bind(RegistrationControl control) {
        this.control = control;
        return this;
    }

    @Override
    public RegistrationControl getControl() {
        return control;
    }
}
