package monsterstack.io.partner.settings;

import android.content.Intent;
import android.content.res.Configuration;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import monsterstack.io.api.UserSessionManager;
import monsterstack.io.partner.R;
import monsterstack.io.partner.menu.SettingsActivity;

public class PinSettingsActivity extends DetailSettingsActivity {
    @BindView(R.id.pinUpdateEdit)
    EditText editText;
    @BindView(R.id.keyboard)
    KeyboardView keyboardView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        editText.setSelection(editText.getText().length());
        editText.setRawInputType(Configuration.KEYBOARD_12KEY);

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
    public int getActionTitle() {
        return R.string.detail_settings_pin;
    }

    @OnClick(R.id.pinUpdateButton)
    public void onPinUpdate(View view) {
        UserSessionManager sessionManager = new UserSessionManager(this);
        String pin = editText.getText().toString();

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
