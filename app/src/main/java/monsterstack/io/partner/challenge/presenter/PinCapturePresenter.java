package monsterstack.io.partner.challenge.presenter;

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
import monsterstack.io.partner.challenge.control.PinCaptureControl;
import monsterstack.io.partner.common.HasProgressBarSupport;
import monsterstack.io.partner.common.presenter.Presenter;
import monsterstack.io.pincapture.PinCapture;

public class PinCapturePresenter implements Presenter<PinCaptureControl>, HasProgressBarSupport {
    @BindView(R.id.pinCaptureEdit)
    PinCapture editText;
    @BindView(R.id.keyboard)
    KeyboardView keyboardView;

    @BindView(R.id.progressbar)
    ProgressBar progressBar;

    private PinCaptureControl control;

    public PinCapturePresenter(PinCaptureControl control) {
        this.control = control;
    }

    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    public String getEnteredPin() {
        return editText.getEnteredText();
    }

    @Override
    public Presenter<PinCaptureControl> present(Optional<Map> metadata) {
        editText.setOnFinishListener(enteredText -> {

        });

        progressBar.setVisibility(View.GONE);
        keyboardView.setActivated(true);
        keyboardView.setEnabled(true);

        return this;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        ((Activity)control.getContext()).getMenuInflater().inflate(R.menu.next_action, menu);

        menu.findItem(R.id.next_button).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                control.onNext();
                return false;
            }
        });
        return true;
    }

    @Override
    public Presenter<PinCaptureControl> bind(PinCaptureControl control) {
        this.control = control;
        return this;
    }

    @Override
    public PinCaptureControl getControl() {
        return control;
    }
}
