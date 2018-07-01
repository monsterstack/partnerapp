package monsterstack.io.partner.settings.presenter;

import android.app.Activity;
import android.inputmethodservice.KeyboardView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import java.util.Map;
import java.util.Optional;

import butterknife.BindView;
import monsterstack.io.partner.R;
import monsterstack.io.partner.common.HasProgressBarSupport;
import monsterstack.io.partner.common.presenter.Presenter;
import monsterstack.io.partner.settings.control.PinSettingsControl;
import monsterstack.io.pincapture.PinCapture;

import static android.view.View.GONE;

public class PinSettingsPresenter implements Presenter<PinSettingsControl>, HasProgressBarSupport {
    @BindView(R.id.pinCaptureEdit)
    PinCapture editText;
    @BindView(R.id.keyboard)
    KeyboardView keyboardView;

    @BindView(R.id.progressbar)
    ProgressBar progressBar;

    private PinSettingsControl control;

    public PinSettingsPresenter(PinSettingsControl control) {
        this.control = control;
    }

    public String getCapturedPin() {
        return editText.getEnteredText();
    }

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(GONE);
    }

    @Override
    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public Presenter<PinSettingsControl> present(Optional<Map> metadata) {
        editText.setOnFinishListener(enteredText -> {

        });

        keyboardView.setActivated(true);
        keyboardView.setEnabled(true);

        return this;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        ((Activity)control.getContext()).getMenuInflater().inflate(R.menu.update_action, menu);

        menu.findItem(R.id.update_button).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                control.onUpdate();
                return false;
            }
        });
        return true;
    }

    @Override
    public Presenter<PinSettingsControl> bind(PinSettingsControl control) {
        this.control = control;
        return this;
    }

    @Override
    public PinSettingsControl getControl() {
        return control;
    }
}
