package monsterstack.io.partner.challenge.presenter;

import android.app.Activity;
import android.content.res.Configuration;
import android.inputmethodservice.KeyboardView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import java.util.Map;
import java.util.Optional;

import butterknife.BindView;
import monsterstack.io.partner.R;
import monsterstack.io.partner.challenge.control.PhoneCaptureControl;
import monsterstack.io.partner.common.HasProgressBarSupport;
import monsterstack.io.partner.common.HasSnackBarSupport;
import monsterstack.io.partner.common.presenter.Presenter;
import monsterstack.io.partner.common.support.TextWatcherAdapter;

import static android.view.View.GONE;

public class PhoneCapturePresenter implements Presenter<PhoneCaptureControl>, HasProgressBarSupport, HasSnackBarSupport {
    @BindView(R.id.keyboard)
    KeyboardView keyboardView;

    @BindView(R.id.phoneCaptureEdit)
    EditText editText;

    @BindView(R.id.progressbar)
    ProgressBar progressBar;

    @BindView(R.id.phone_capture)
    View rootView;

    private PhoneCaptureControl control;

    public PhoneCapturePresenter(PhoneCaptureControl control) {
        this.control = control;
    }

    @Override
    public View getRootView() {
        return rootView;
    }

    @Override
    public Presenter<PhoneCaptureControl> present(Optional<Map> metadata) {
        progressBar.setVisibility(GONE);

        editText.setSelection(editText.getText().length());
        editText.setRawInputType(Configuration.KEYBOARD_12KEY);

        editText.addTextChangedListener(new TextWatcherAdapter() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (control.isValidPhoneNumber(s.toString(), "US")) {
                    markPhoneNumberGood();
                } else {
                    markPhoneNumberBad();
                }
            }
        });

        keyboardView.setActivated(true);
        keyboardView.setEnabled(true);

        return this;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        ((Activity)control.getContext()).getMenuInflater().inflate(R.menu.next_action, menu);

        MenuItem nextButton = menu.findItem(R.id.next_button);

        nextButton.setOnMenuItemClickListener(item -> {
            control.onCapture();
            return false;
        });

        return true;
    }

    public void hideProgressBar() {
        progressBar.setVisibility(GONE);
    }

    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    public String getPhoneNumber() {
        return editText.getText().toString();
    }

    @Override
    public Presenter<PhoneCaptureControl> bind(PhoneCaptureControl control) {
        this.control = control;
        return this;
    }

    @Override
    public PhoneCaptureControl getControl() {
        return control;
    }

    private void markPhoneNumberGood() {
        editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_check_black_24dp, 0);
        showReaffirmation("Good Phone Number");
    }

    private void markPhoneNumberBad() {
        editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_warning_black_24dp, 0);
        showError("Invalid Phone Number");
    }
}
