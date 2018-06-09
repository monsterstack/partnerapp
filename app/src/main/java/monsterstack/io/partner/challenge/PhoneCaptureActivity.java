package monsterstack.io.partner.challenge;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import butterknife.ButterKnife;
import monsterstack.io.api.custom.ChallengeServiceCustom;
import monsterstack.io.api.listeners.OnResponseListener;
import monsterstack.io.api.resources.Challenge;
import monsterstack.io.api.resources.HttpError;
import monsterstack.io.partner.R;
import monsterstack.io.partner.challenge.presenter.PhoneCapturePresenter;
import monsterstack.io.partner.common.BasicActivity;

public class PhoneCaptureActivity extends BasicActivity {

    protected PhoneCapturePresenter presenter;

    @Override
    public int getContentView() {
        return R.layout.phone_capture;
    }

    @Override
    public AppCompatActivity getActivity() {
        return this;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new PhoneCapturePresenter();
        ButterKnife.bind(presenter, this);

        presenter.present();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.next_action, menu);

        MenuItem nextButton = menu.findItem(R.id.next_button);

        nextButton.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                onCapture();
                return false;
            }
        });

        return true;
    }

    @Override
    public int getActionTitle() {
        return R.string.phone_capture;
    }

    public void onCapture() {
        presenter.showProgressBar();

        Challenge challenge = new Challenge();
        challenge.setPhoneNumber(presenter.getPhoneNumber());
        ChallengeServiceCustom challengeService = getServiceLocator().getChallengeService();

        challengeService.submitChallenge(challenge, onResponseListener());
    }

    private OnResponseListener<Void, HttpError> onResponseListener() {
        return new OnResponseListener<Void, HttpError>() {
            @Override
            public void onResponse(Void aVoid, HttpError httpError) {
                if (null == aVoid && null == httpError) {
                    Intent intent = new Intent(PhoneCaptureActivity.this,
                            ChallengeVerificationActivity.class);
                    /* Copy all extras */
                    intent.putExtras(getIntent().getExtras());
                    applySourceToIntent(intent, PhoneCaptureActivity.class);

                    startActivity(intent, enterStageRightBundle());
                    presenter.hideProgressBar();

                } else {
                    showHttpError(getResources().getString(getActionTitle()), httpError);
                    presenter.hideProgressBar();
                }
            }
        };
    }
}
