package monsterstack.io.partner.registration.presenter.impl;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
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
import monsterstack.io.partner.common.presenter.Presenter;
import monsterstack.io.partner.common.support.TextWatcherAdapter;
import monsterstack.io.partner.registration.RegistrationNameCaptureActivity;
import monsterstack.io.partner.registration.control.RegistrationNameCaptureControl;
import monsterstack.io.partner.registration.presenter.RegistrationNameCapturePresenter;

public class RegistrationNameCapturePresenterImpl implements RegistrationNameCapturePresenter {
    @BindView(R.id.first_name)
    EditText firstNameEdit;

    @BindView(R.id.registration_last_name)
    EditText lastNameEdit;

    @BindView(R.id.progressbar)
    ProgressBar progressBar;

    @BindView(R.id.registration_progress)
    ProgressBar registrationProgress;

    @BindView(R.id.my_toolbar)
    Toolbar toolbar;

    @BindView(R.id.registration_name_capture)
    View view;

    private Context context;

    private RegistrationNameCaptureControl control;

    public RegistrationNameCapturePresenterImpl(Context context) {
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
    public View getRootView() {
        return view;
    }

    @Override
    public String getFirstName() {
        return firstNameEdit.getText().toString();
    }

    @Override
    public String getLastName() {
        return lastNameEdit.getText().toString();
    }

    @Override
    public Presenter<RegistrationNameCaptureControl> present(Optional<Map> metadata) {
        hideProgressBar();
        firstNameEdit.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        lastNameEdit.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        changeColor(registrationProgress, ContextCompat.getColor(getControl().getContext(), R.color.lightGreen));

        this.firstNameEdit.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 0) {
                    RegistrationNameCapturePresenterImpl.this.markFirstNameGood();
                    if (!lastNameEdit.getText().toString().isEmpty()) {
                        registrationProgress.setProgress(50);
                    } else {
                        registrationProgress.setProgress(25);
                    }
                } else {
                    RegistrationNameCapturePresenterImpl.this.markFirstNameBad();
                    registrationProgress.setProgress(
                            registrationProgress.getProgress() - 25
                    );
                }
            }
        });

        this.lastNameEdit.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    RegistrationNameCapturePresenterImpl.this.markLastNameGood();
                    if (!firstNameEdit.getText().toString().isEmpty()) {
                        registrationProgress.setProgress(50);
                    } else {
                        registrationProgress.setProgress(25);
                    }
                } else {
                    RegistrationNameCapturePresenterImpl.this.markLastNameBad();
                    registrationProgress.setProgress(
                            registrationProgress.getProgress() - 25
                    );
                }
            }
        });
        return this;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        ((Activity)context).getMenuInflater().inflate(R.menu.next_action, menu);

        menu.findItem(R.id.next_button).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                ((RegistrationNameCaptureActivity)context).onNext(item);
                return false;
            }
        });
        return true;
    }

    public Registration buildRegistrationFromBindingData() {
        String firstName = firstNameEdit.getText().toString();
        String lastName = lastNameEdit.getText().toString();

        Registration registration = new Registration();
        MinimalUser minimalUser = new MinimalUser();
        minimalUser.setFirstName(firstName);
        minimalUser.setLastName(lastName);

        registration.setUser(minimalUser);

        return registration;
    }

    @Override
    public Presenter<RegistrationNameCaptureControl> bind(RegistrationNameCaptureControl control) {
        this.control = control;
        return this;
    }

    @Override
    public RegistrationNameCaptureControl getControl() {
        return control;
    }

    private void markFirstNameGood() {
        this.firstNameEdit.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_check_black_24dp, 0);
    }

    private void markLastNameGood() {
        this.lastNameEdit.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_check_black_24dp, 0);
    }

    private void markFirstNameBad() {
        this.firstNameEdit.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_warning_black_24dp, 0);
        showError(view, "Missing First Name");
    }

    private void markLastNameBad() {
        this.lastNameEdit.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_warning_black_24dp, 0);
        showError(view, "Missing Last Name");
    }
}
