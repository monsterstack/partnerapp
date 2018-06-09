package monsterstack.io.partner.challenge.presenter;

import android.view.View;
import android.widget.ProgressBar;

import butterknife.BindView;
import monsterstack.io.partner.R;
import monsterstack.io.pincapture.PinCapture;

import static android.view.View.GONE;

public class ChallengeVerificationPresenter {
    @BindView(R.id.challengeVerificationEdit)
    PinCapture editText;

    @BindView(R.id.progressbar)
    ProgressBar progressBar;

    public void hideProgressBar() {
        progressBar.setVisibility(GONE);
    }

    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    public String getCapturedCode() {
        return editText.getEnteredText();
    }

    public void present() {
        this.hideProgressBar();

        editText.setOnFinishListener(new PinCapture.OnFinishListener() {
            @Override
            public void onFinish(String enteredText) {

            }
        });
    }
}
