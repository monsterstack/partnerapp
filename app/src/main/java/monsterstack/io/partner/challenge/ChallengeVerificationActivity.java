package monsterstack.io.partner.challenge;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

import java.util.Optional;

import butterknife.ButterKnife;
import monsterstack.io.api.custom.ChallengeServiceCustom;
import monsterstack.io.api.listeners.OnResponseListener;
import monsterstack.io.api.resources.Challenge;
import monsterstack.io.api.resources.HttpError;
import monsterstack.io.partner.MainActivity;
import monsterstack.io.partner.R;
import monsterstack.io.partner.challenge.control.ChallengeVerificationControl;
import monsterstack.io.partner.challenge.presenter.ChallengeVerificationPresenter;
import monsterstack.io.partner.common.BasicActivity;
import monsterstack.io.partner.menu.SettingsActivity;
import monsterstack.io.partner.settings.MobileNumberSettingsActivity;

public class ChallengeVerificationActivity extends BasicActivity implements ChallengeVerificationControl {

    protected ChallengeVerificationPresenter presenter;

    @Override
    protected void onCreate(Bundle savedBundleState) {
        super.onCreate(savedBundleState);
        presenter = new ChallengeVerificationPresenter(this);
        ButterKnife.bind(presenter, this);

        presenter.present(Optional.empty());
    }

    @Override
    public int getContentView() {
        return R.layout.challenge_verification;
    }

    @Override
    public void setUpTransitions() {

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
        return R.string.challenge_verification;
    }

    @Override
    public AppCompatActivity getActivity() {
        return this;
    }

    @Override
    public void onVerify() {
        if(!isValidChallengeCode(presenter.getCapturedCode())) {
            presenter.showError("Challenge code required to proceed");
        } else {
            presenter.showProgressBar();

            final String source = (String) getIntent().getExtras().get("source");

            ChallengeServiceCustom challengeServiceCustom = getServiceLocator().getChallengeService();

            String code = presenter.getCapturedCode();
            challengeServiceCustom.verifyChallengeCode(code, new OnResponseListener<Challenge, HttpError>() {
                @Override
                public void onResponse(Challenge challenge, HttpError httpError) {
                    if (null != challenge) {
                        if (source.equals(PhoneCaptureActivity.class.getCanonicalName())) {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent, enterStageRightBundle());
                            presenter.showProgressBar();
                        } else if (source.equals(MobileNumberSettingsActivity.class.getCanonicalName())) {
                            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent, exitStageLeftBundle());
                            presenter.hideProgressBar();
                        }
                    } else {
                        if (httpError.getStatusCode() == 404) {
                            showHttpError(getResources().getString(getActionTitle()),
                                    getResources().getString(R.string.verification_code_not_found),
                                    httpError);
                        } else {
                            showHttpError(getResources().getString(getActionTitle()),
                                    httpError);
                        }

                        presenter.hideProgressBar();
                    }
                }
            });

        }

    }
}
