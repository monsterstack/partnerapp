package monsterstack.io.partner.settings.presenter;

import android.content.res.Configuration;
import android.inputmethodservice.KeyboardView;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import java.util.Map;
import java.util.Optional;

import butterknife.BindView;
import monsterstack.io.api.UserSessionManager;
import monsterstack.io.api.resources.AuthenticatedUser;
import monsterstack.io.partner.R;
import monsterstack.io.partner.common.HasProgressBarSupport;
import monsterstack.io.partner.common.presenter.Presenter;
import monsterstack.io.partner.settings.control.MobileNumberSettingsControl;

import static android.view.View.GONE;

public class MobileNumberSettingsPresenter implements Presenter<MobileNumberSettingsControl>, HasProgressBarSupport {
    @BindView(R.id.mobileEdit)
    EditText editText;

    @BindView(R.id.keyboard)
    KeyboardView keyboardView;

    @BindView(R.id.progressbar)
    ProgressBar progressBar;

    private MobileNumberSettingsControl control;

    public MobileNumberSettingsPresenter(MobileNumberSettingsControl control) {
        this.control = control;
    }

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(GONE);
    }

    @Override
    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public Presenter<MobileNumberSettingsControl> present(Optional<Map> metadata) {
        editText.setSelection(editText.getText().length());
        editText.setRawInputType(Configuration.KEYBOARD_12KEY);

        keyboardView.setActivated(true);
        keyboardView.setEnabled(true);

        UserSessionManager userSessionManager = new UserSessionManager(control.getContext());
        AuthenticatedUser user = userSessionManager.getUserDetails();

        if(null != user.getPhoneNumber()) {
            editText.setText(user.getPhoneNumber());
        }

        return this;
    }

    public String getCapturedPhoneNumber() {
        return editText.getText().toString();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public Presenter<MobileNumberSettingsControl> bind(MobileNumberSettingsControl control) {
        this.control = control;

        return this;
    }

    @Override
    public MobileNumberSettingsControl getControl() {
        return control;
    }
}
