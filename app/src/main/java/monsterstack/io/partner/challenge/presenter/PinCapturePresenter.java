package monsterstack.io.partner.challenge.presenter;

import android.inputmethodservice.KeyboardView;
import android.view.View;
import android.widget.ProgressBar;

import butterknife.BindView;
import monsterstack.io.partner.R;
import monsterstack.io.pincapture.PinCapture;

public class PinCapturePresenter {
    @BindView(R.id.pinCaptureEdit)
    PinCapture editText;
    @BindView(R.id.keyboard)
    KeyboardView keyboardView;

    @BindView(R.id.progressbar)
    ProgressBar progressBar;

    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    public String getEnteredPin() {
        return editText.getEnteredText();
    }

    public void present() {
        editText.setOnFinishListener(new PinCapture.OnFinishListener() {
            @Override
            public void onFinish(String enteredText) {

            }
        });

        progressBar.setVisibility(View.GONE);
        keyboardView.setActivated(true);
        keyboardView.setEnabled(true);
    }
}
