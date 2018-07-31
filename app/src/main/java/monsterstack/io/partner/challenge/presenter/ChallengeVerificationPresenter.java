package monsterstack.io.partner.challenge.presenter;

import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import java.util.Map;
import java.util.Optional;

import butterknife.BindView;
import monsterstack.io.partner.R;
import monsterstack.io.partner.challenge.control.ChallengeVerificationControl;
import monsterstack.io.partner.common.HasProgressBarSupport;
import monsterstack.io.partner.common.HasSnackBarSupport;
import monsterstack.io.partner.common.presenter.Presenter;
import monsterstack.io.pincapture.PinCapture;

import static android.view.View.GONE;

public class ChallengeVerificationPresenter implements Presenter<ChallengeVerificationControl>, HasProgressBarSupport, HasSnackBarSupport {
    @BindView(R.id.challengeVerificationEdit)
    PinCapture editText;

    @BindView(R.id.progressbar)
    ProgressBar progressBar;

    @BindView(R.id.challenge_verification)
    View rootView;

    private ChallengeVerificationControl control;

    public ChallengeVerificationPresenter(ChallengeVerificationControl control) {
        this.control = control;
    }

    public void hideProgressBar() {
        progressBar.setVisibility(GONE);
    }

    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    public String getCapturedCode() {
        return editText.getEnteredText();
    }

    @Override
    public View getRootView() {
        return rootView;
    }

    @Override
    public Presenter<ChallengeVerificationControl> present(Optional<Map> metadata) {
        this.hideProgressBar();

        editText.setOnFinishListener(enteredText -> {

        });

        return this;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        ((Activity)control.getContext()).getMenuInflater().inflate(R.menu.submit_action, menu);

        MenuItem submitButton = menu.findItem(R.id.submit_button);

        submitButton.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                control.onVerify();
                return false;
            }
        });

        return true;
    }

    @Override
    public Presenter<ChallengeVerificationControl> bind(ChallengeVerificationControl control) {
        this.control = control;
        return this;
    }

    @Override
    public ChallengeVerificationControl getControl() {
        return control;
    }
}
