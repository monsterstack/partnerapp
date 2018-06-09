package monsterstack.io.partner.challenge.presenter;

import android.content.res.Configuration;
import android.inputmethodservice.KeyboardView;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import butterknife.BindView;
import monsterstack.io.partner.R;

import static android.view.View.GONE;

public class PhoneCapturePresenter {
    @BindView(R.id.keyboard)
    KeyboardView keyboardView;

    @BindView(R.id.phoneCaptureEdit)
    EditText editText;

    @BindView(R.id.progressbar)
    ProgressBar progressBar;


    public void present() {
        progressBar.setVisibility(GONE);

        editText.setSelection(editText.getText().length());
        editText.setRawInputType(Configuration.KEYBOARD_12KEY);

        keyboardView.setActivated(true);
        keyboardView.setEnabled(true);
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
}
