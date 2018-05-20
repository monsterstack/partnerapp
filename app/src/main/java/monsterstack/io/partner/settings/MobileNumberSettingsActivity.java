package monsterstack.io.partner.settings;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.res.Configuration;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import monsterstack.io.api.ServiceLocator;
import monsterstack.io.api.UserSessionManager;
import monsterstack.io.api.custom.ChallengeServiceCustom;
import monsterstack.io.api.listeners.OnResponseListener;
import monsterstack.io.api.resources.AuthenticatedUser;
import monsterstack.io.api.resources.Challenge;
import monsterstack.io.api.resources.HttpError;
import monsterstack.io.partner.R;
import monsterstack.io.partner.challenge.MobileNumberUpdateChallengeVerificationActivity;

import static android.view.View.GONE;

public class MobileNumberSettingsActivity extends DetailSettingsActivity {

    @BindView(R.id.mobileEdit)
    EditText editText;

    @BindView(R.id.keyboard)
    KeyboardView keyboardView;

    @BindView(R.id.progressbar)
    ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);

        editText.setSelection(editText.getText().length());
        editText.setRawInputType(Configuration.KEYBOARD_12KEY);

        keyboardView.setActivated(true);
        keyboardView.setEnabled(true);

        UserSessionManager userSessionManager = new UserSessionManager(this);
        AuthenticatedUser user = userSessionManager.getUserDetails();

        if(null != user.getPhoneNumber()) {
            editText.setText(user.getPhoneNumber());
        }
    }

    public int getContentView() {
        return R.layout.phone_settings;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.next_action, menu);

        menu.findItem(R.id.next_button).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                onNext();
                return false;
            }
        });
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void setUpTransitions() {

    }

    @Override
    public int getActionTitle() {
        return R.string.detail_settings_mobileNumber;
    }

    public void onNext() {
        ServiceLocator serviceLocator = getServiceLocator();
        ChallengeServiceCustom challengeService = serviceLocator.getChallengeService();

        String phoneNumber = editText.getText().toString();

        Challenge challenge = new Challenge();
        challenge.setPhoneNumber(phoneNumber);
        progressBar.setVisibility(View.VISIBLE);

        challengeService.submitChallenge(challenge, onResponseListener());

    }

    OnResponseListener<Void, HttpError> onResponseListener() {
        return new OnResponseListener<Void, HttpError>() {
            @Override
            public void onResponse(Void aVoid, HttpError httpError) {
                if (null == aVoid && null == httpError) {
                    Bundle bundle = ActivityOptions.makeCustomAnimation(
                            getApplicationContext(), R.anim.slide_left, R.anim.slide_right).toBundle();
                    Intent intent = new Intent(MobileNumberSettingsActivity.this,
                            MobileNumberUpdateChallengeVerificationActivity.class);
                    intent.putExtra("source", MobileNumberSettingsActivity.class.getCanonicalName());

                    startActivity(intent, bundle);
                    progressBar.setVisibility(GONE);

                } else {
                    showHttpError(getResources().getString(getActionTitle()), httpError);
                    progressBar.setVisibility(GONE);
                }
            }
        };
    }
}
