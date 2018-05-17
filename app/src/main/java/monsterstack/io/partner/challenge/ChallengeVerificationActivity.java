package monsterstack.io.partner.challenge;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import monsterstack.io.api.ServiceLocator;
import monsterstack.io.api.custom.ChallengeServiceCustom;
import monsterstack.io.api.listeners.OnResponseListener;
import monsterstack.io.api.resources.Challenge;
import monsterstack.io.api.resources.HttpError;
import monsterstack.io.partner.MainActivity;
import monsterstack.io.partner.R;
import monsterstack.io.partner.common.BasicActivity;
import monsterstack.io.partner.menu.SettingsActivity;
import monsterstack.io.partner.settings.MobileNumberSettingsActivity;

import static android.view.View.GONE;

public class ChallengeVerificationActivity extends BasicActivity {
    @BindView(R.id.challengeVerificationEdit)
    EditText editText;

    @BindView(R.id.progressbar)
    ProgressBar progressBar;

    private ServiceLocator serviceLocator;

    @Override
    protected void onCreate(Bundle savedBundleState) {
        super.onCreate(savedBundleState);
        this.serviceLocator = ServiceLocator.getInstance(getApplicationContext());

        ButterKnife.bind(this);

        progressBar.setVisibility(GONE);

        editText.setSelection(editText.getText().length());
        editText.setRawInputType(Configuration.KEYBOARD_12KEY);
    }

    @Override
    public int getContentView() {
        return R.layout.challenge_verification;
    }

    @Override
    public void setUpTransitions() {

    }

    @Override
    public int getActionTitle() {
        return R.string.challenge_verification;
    }

    @Override
    public AppCompatActivity getActivity() {
        return this;
    }

    @OnClick(R.id.challengeVerificationButton)
    public void onClick(View view) {
        progressBar.setVisibility(View.VISIBLE);

        final String source = (String)getIntent().getExtras().get("source");

        ChallengeServiceCustom challengeServiceCustom = serviceLocator.getChallengeService();

        String code = editText.getText().toString();
        challengeServiceCustom.verifyChallengeCode(code, new OnResponseListener<Challenge, HttpError>() {
            @Override
            public void onResponse(Challenge challenge, HttpError httpError) {
                if (null != challenge) {
                    if (source.equals(PhoneCaptureActivity.class.getCanonicalName())) {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent, enterStageRightBundle());
                        progressBar.setVisibility(GONE);
                    } else if (source.equals(MobileNumberSettingsActivity.class.getCanonicalName())) {
                        Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent, exitStageLeftBundle());
                        progressBar.setVisibility(GONE);
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

                    progressBar.setVisibility(GONE);
                }
            }
        });

    }
}
