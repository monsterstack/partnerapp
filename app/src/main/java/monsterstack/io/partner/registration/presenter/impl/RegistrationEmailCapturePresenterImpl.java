package monsterstack.io.partner.registration.presenter.impl;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputType;
import android.view.Menu;
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
import monsterstack.io.partner.registration.RegistrationEmailCaptureActivity;
import monsterstack.io.partner.registration.control.RegistrationEmailCaptureControl;
import monsterstack.io.partner.registration.presenter.RegistrationEmailCapturePresenter;

public class RegistrationEmailCapturePresenterImpl implements RegistrationEmailCapturePresenter {
    @BindView(R.id.email_address)
    EditText emailEdit;

    @BindView(R.id.progressbar)
    ProgressBar progressBar;

    @BindView(R.id.registration_progress)
    ProgressBar registrationProgress;

    @BindView(R.id.registration_email_capture)
    View view;

    private RegistrationEmailCaptureControl control;

    @Override
    public Presenter<RegistrationEmailCaptureControl> present(Optional<Map> metadata) {
        hideProgressBar();
        emailEdit.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        changeColor(registrationProgress, ContextCompat.getColor(getControl().getContext(), R.color.lightGreen));

        emailEdit.addTextChangedListener(new TextWatcherAdapter() {

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0 && control.isValidEmail(s.toString())) {
                    markEmailGood();
                    showReaffirmation("Good email");
                    if (!emailEdit.getText().toString().isEmpty()) {
                        registrationProgress.setProgress(100);
                    } else {
                        registrationProgress.setProgress(50);
                    }
                } else {
                    markEmailBad();
                    registrationProgress.setProgress(
                            registrationProgress.getProgress() - 50
                    );
                }
            }
        });
        return this;
    }

    @Override
    public View getRootView() {
        return view;
    }

    @Override
    public Integer getProgress() {
        return registrationProgress.getProgress();
    }

    @Override
    public String getEmailAddress() {
        return emailEdit.getText().toString();
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
    public Presenter<RegistrationEmailCaptureControl> bind(RegistrationEmailCaptureControl control) {
        this.control = control;
        return this;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        ((Activity)control.getContext()).getMenuInflater().inflate(R.menu.next_action, menu);

        menu.findItem(R.id.next_button).setOnMenuItemClickListener(item -> {
            ((RegistrationEmailCaptureActivity)control.getContext()).onNext(item);
            return false;
        });
        return true;
    }

    @Override
    public RegistrationEmailCaptureControl getControl() {
        return this.control;
    }

    public Registration buildRegistrationFromBindingData() {
        String emailAddress = emailEdit.getText().toString();

        Registration registration = new Registration();
        MinimalUser minimalUser = new MinimalUser();
        minimalUser.setEmailAddress(emailAddress);
        registration.setUser(minimalUser);

        return registration;
    }

    public void markEmailBad() {
        this.emailEdit.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_warning_black_24dp, 0);
        showError(view, "Invalid Email Address");
    }

    public void markEmailGood() {
        this.emailEdit.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_check_black_24dp, 0);
    }

}
