package monsterstack.io.partner.settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;

import java.util.Map;
import java.util.Optional;

import butterknife.ButterKnife;
import monsterstack.io.api.UserSessionManager;
import monsterstack.io.partner.R;
import monsterstack.io.partner.menu.SettingsActivity;
import monsterstack.io.partner.settings.control.PinSettingsControl;
import monsterstack.io.partner.settings.presenter.PinSettingsPresenter;

public class PinSettingsActivity extends DetailSettingsActivity implements PinSettingsControl {
    protected PinSettingsPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new PinSettingsPresenter(this);
        ButterKnife.bind(presenter, this);

        presenter.present(Optional.<Map>empty());
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
        return presenter.onCreateOptionsMenu(menu);
    }

    @Override
    public int getActionTitle() {
        return R.string.detail_settings_pin;
    }

    @Override
    public Context getContext() { return this; }

    @Override
    public void onUpdate() {
        UserSessionManager sessionManager = new UserSessionManager(this);
        String pin = presenter.getCapturedPin();

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
