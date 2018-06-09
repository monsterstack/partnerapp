package monsterstack.io.partner.challenge;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import butterknife.ButterKnife;
import monsterstack.io.api.ServiceLocator;
import monsterstack.io.api.custom.ChallengeServiceCustom;
import monsterstack.io.api.listeners.OnResponseListener;
import monsterstack.io.api.resources.Challenge;
import monsterstack.io.api.resources.HttpError;
import monsterstack.io.partner.MainActivity;
import monsterstack.io.partner.R;
import monsterstack.io.partner.challenge.presenter.ChallengeVerificationPresenter;
import monsterstack.io.partner.common.BasicActivity;
import monsterstack.io.partner.menu.SettingsActivity;
import monsterstack.io.partner.settings.MobileNumberSettingsActivity;

public class ChallengeVerificationActivity extends BasicActivity {

    protected ChallengeVerificationPresenter presenter;
    private ServiceLocator serviceLocator;

    @Override
    protected void onCreate(Bundle savedBundleState) {
        super.onCreate(savedBundleState);
        this.serviceLocator = ServiceLocator.getInstance(getApplicationContext());
        presenter = new ChallengeVerificationPresenter();
        ButterKnife.bind(presenter, this);

        presenter.present();
    }

    @Override
    public int getContentView() {
        return R.layout.challenge_verification;
    }

    @Override
    public void setUpTransitions() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.submit_action, menu);

        MenuItem submitButton = menu.findItem(R.id.submit_button);

        submitButton.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                onVerify();
                return false;
            }
        });

        return true;
    }

    @Override
    public int getActionTitle() {
        return R.string.challenge_verification;
    }

    @Override
    public AppCompatActivity getActivity() {
        return this;
    }

    public void onVerify() {
        presenter.showProgressBar();

        final String source = (String)getIntent().getExtras().get("source");

        ChallengeServiceCustom challengeServiceCustom = serviceLocator.getChallengeService();

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
