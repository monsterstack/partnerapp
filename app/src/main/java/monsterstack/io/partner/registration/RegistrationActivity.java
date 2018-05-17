package monsterstack.io.partner.registration;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import monsterstack.io.api.ServiceLocator;
import monsterstack.io.api.resources.MinimalUser;
import monsterstack.io.api.resources.Registration;
import monsterstack.io.partner.R;
import monsterstack.io.partner.challenge.RegistrationPhoneCaptureActivity;
import monsterstack.io.partner.common.BasicActivity;
import monsterstack.io.partner.utils.NavigationUtils;

public class RegistrationActivity extends BasicActivity {
    @BindView(R.id.signUpButton)
    Button signUpButton;

    @BindView(R.id.first_name)
    EditText firstNameEdit;

    @BindView(R.id.last_name)
    EditText lastNameEdit;

    @BindView(R.id.email_address)
    EditText emailEdit;

    @BindView(R.id.progressbar)
    ProgressBar progressBar;

    private ServiceLocator serviceLocator;

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
        serviceLocator = ServiceLocator.getInstance(this);
        ButterKnife.bind(this);

        progressBar.setVisibility(View.GONE);
    }

    @Override
    public int getActionTitle() {
        return R.string.registration;
    }

    @OnClick(R.id.signUpButton)
    public void onSignUp(View view){
        progressBar.setVisibility(View.VISIBLE);

        final Registration registration = buildRegistrationFromBindingData();

        Bundle bundle = NavigationUtils.enterStageRightBundle(getApplicationContext());
        Intent intent = new Intent(RegistrationActivity.this, RegistrationPhoneCaptureActivity.class);
        intent.putExtra("registration", registration);
        startActivity(intent, bundle);
        progressBar.setVisibility(View.GONE);
    }

    private Registration buildRegistrationFromBindingData() {
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
}
