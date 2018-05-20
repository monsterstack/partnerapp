package monsterstack.io.partner.settings;

import android.content.Intent;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import monsterstack.io.api.UserSessionManager;
import monsterstack.io.partner.R;
import monsterstack.io.partner.menu.SettingsActivity;
import monsterstack.io.pincapture.PinCapture;

public class PinSettingsActivity extends DetailSettingsActivity {
    @BindView(R.id.pinCaptureEdit)
    PinCapture editText;
    @BindView(R.id.keyboard)
    KeyboardView keyboardView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        editText.setOnFinishListener(new PinCapture.OnFinishListener() {
            @Override
            public void onFinish(String enteredText) {

            }
        });

        keyboardView.setActivated(true);
        keyboardView.setEnabled(true);
    }

    @Override
    public int getContentView() {
        return R.layout.pin_settings;
    }

    @Override
    public void setUpTransitions() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.update_action, menu);

        menu.findItem(R.id.update_button).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                onUpdate();
                return false;
            }
        });
        return true;
    }

    @Override
    public int getActionTitle() {
        return R.string.detail_settings_pin;
    }

    public void onUpdate() {
        UserSessionManager sessionManager = new UserSessionManager(this);
        String pin = editText.getEnteredText();

        if(null != pin) {
            sessionManager.createUserPin(pin);
            Intent intent = new Intent(PinSettingsActivity.this, SettingsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent, exitStageLeftBundle());
        } else {
            showError(getResources().getString(getActionTitle()), "Invalid PIN!");
        }
    }
}
