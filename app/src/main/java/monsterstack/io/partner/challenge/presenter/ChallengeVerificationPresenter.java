package monsterstack.io.partner.challenge.presenter;

import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import java.util.Map;
import java.util.Optional;

import butterknife.BindView;
import monsterstack.io.partner.R;
import monsterstack.io.partner.challenge.ChallengeVerificationActivity;
import monsterstack.io.partner.common.HasProgressBarSupport;
import monsterstack.io.partner.common.presenter.PresenterAdapter;
import monsterstack.io.pincapture.PinCapture;

import static android.view.View.GONE;

public class ChallengeVerificationPresenter extends PresenterAdapter implements HasProgressBarSupport {
    @BindView(R.id.challengeVerificationEdit)
    PinCapture editText;

    @BindView(R.id.progressbar)
    ProgressBar progressBar;

    private Context context;

    public ChallengeVerificationPresenter(Context context) {
        this.context = context;
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
    public void present(Optional<Map> metadata) {
        this.hideProgressBar();

        editText.setOnFinishListener(new PinCapture.OnFinishListener() {
            @Override
            public void onFinish(String enteredText) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        ((Activity)context).getMenuInflater().inflate(R.menu.submit_action, menu);

        MenuItem submitButton = menu.findItem(R.id.submit_button);

        submitButton.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                ((ChallengeVerificationActivity)context).onVerify();
                return false;
            }
        });

        return true;
    }
}
