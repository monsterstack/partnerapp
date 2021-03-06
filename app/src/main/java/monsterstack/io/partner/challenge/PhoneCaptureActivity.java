package monsterstack.io.partner.challenge;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

import java.util.Map;
import java.util.Optional;

import butterknife.ButterKnife;
import monsterstack.io.api.custom.ChallengeServiceCustom;
import monsterstack.io.api.listeners.OnResponseListener;
import monsterstack.io.api.resources.Challenge;
import monsterstack.io.api.resources.HttpError;
import monsterstack.io.partner.R;
import monsterstack.io.partner.challenge.control.PhoneCaptureControl;
import monsterstack.io.partner.challenge.presenter.PhoneCapturePresenter;
import monsterstack.io.partner.common.BasicActivity;

public class PhoneCaptureActivity extends BasicActivity implements PhoneCaptureControl {

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
        presenter = new PhoneCapturePresenter(this);
        ButterKnife.bind(presenter, this);

        presenter.present(Optional.<Map>empty());
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return presenter.onCreateOptionsMenu(menu);
    }

    @Override
    public int getActionTitle() {
        return R.string.phone_capture;
    }

    @Override
    public void onCapture() {
        if (this.presenter.getPhoneNumber().isEmpty()) {
            presenter.showError("Phone number is required to proceed");
        } else {
            presenter.showProgressBar();

            Challenge challenge = new Challenge();
            challenge.setPhoneNumber(presenter.getPhoneNumber());
            ChallengeServiceCustom challengeService = getServiceLocator().getChallengeService();

            challengeService.submitChallenge(challenge, onResponseListener());
        }
    }

    private OnResponseListener<Void, HttpError> onResponseListener() {
        return (aVoid, httpError) -> {
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
        };
    }
}
