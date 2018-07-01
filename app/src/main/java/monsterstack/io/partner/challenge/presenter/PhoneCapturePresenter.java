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
import monsterstack.io.partner.common.presenter.Presenter;

import static android.view.View.GONE;

public class PhoneCapturePresenter implements Presenter<PhoneCaptureControl>, HasProgressBarSupport {
    @BindView(R.id.keyboard)
    KeyboardView keyboardView;

    @BindView(R.id.phoneCaptureEdit)
    EditText editText;

    @BindView(R.id.progressbar)
    ProgressBar progressBar;

    private PhoneCaptureControl control;

    public PhoneCapturePresenter(PhoneCaptureControl control) {
        this.control = control;
    }

    @Override
    public Presenter<PhoneCaptureControl> present(Optional<Map> metadata) {
        progressBar.setVisibility(GONE);

        editText.setSelection(editText.getText().length());
        editText.setRawInputType(Configuration.KEYBOARD_12KEY);

        keyboardView.setActivated(true);
        keyboardView.setEnabled(true);

        return this;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        ((Activity)control.getContext()).getMenuInflater().inflate(R.menu.next_action, menu);

        MenuItem nextButton = menu.findItem(R.id.next_button);

        nextButton.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                control.onCapture();
                return false;
            }
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
}
