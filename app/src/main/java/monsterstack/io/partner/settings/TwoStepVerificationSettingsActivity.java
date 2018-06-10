package monsterstack.io.partner.settings;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.Map;
import java.util.Optional;

import butterknife.ButterKnife;
import monsterstack.io.partner.R;
import monsterstack.io.partner.settings.control.TwoStepVerificationSettingsControl;
import monsterstack.io.partner.settings.presenter.TwoStepVerificationSettingsPresenter;

public class TwoStepVerificationSettingsActivity extends DetailSettingsActivity implements TwoStepVerificationSettingsControl {
    private TwoStepVerificationSettingsPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new TwoStepVerificationSettingsPresenter(this);
        ButterKnife.bind(presenter, this);

        presenter.present(Optional.<Map>empty());
    }

    @Override
    public int getContentView() {
        return R.layout.two_step_verification_settings;
    }

    @Override
    public void setUpTransitions() {

    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public int getActionTitle() {
        return R.string.detail_settings_two_step_verify;
    }

}
