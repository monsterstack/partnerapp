package monsterstack.io.partner.challenge;

import android.content.Intent;
import android.content.res.Configuration;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import monsterstack.io.api.custom.ChallengeServiceCustom;
import monsterstack.io.api.listeners.OnResponseListener;
import monsterstack.io.api.resources.Challenge;
import monsterstack.io.api.resources.HttpError;
import monsterstack.io.partner.R;
import monsterstack.io.partner.common.BasicActivity;

import static android.view.View.GONE;

public class PhoneCaptureActivity extends BasicActivity {
    @BindView(R.id.keyboard)
    KeyboardView keyboardView;

    @BindView(R.id.phoneCaptureEdit)
    EditText editText;

    @BindView(R.id.progressbar)
    ProgressBar progressBar;

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

        ButterKnife.bind(this);

        progressBar.setVisibility(GONE);

        editText.setSelection(editText.getText().length());
        editText.setRawInputType(Configuration.KEYBOARD_12KEY);

        keyboardView.setActivated(true);
        keyboardView.setEnabled(true);
    }

    @Override
    public int getActionTitle() {
        return R.string.phone_capture;
    }

    @OnClick(R.id.phoneCaptureButton)
    public void onCapture(View view) {
        progressBar.setVisibility(View.VISIBLE);

        Challenge challenge = new Challenge();
        challenge.setPhoneNumber(editText.getText().toString());
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
                    progressBar.setVisibility(GONE);

                } else {
                    showHttpError(getResources().getString(getActionTitle()), httpError);
                    progressBar.setVisibility(GONE);
                }
            }
        };
    }
}
