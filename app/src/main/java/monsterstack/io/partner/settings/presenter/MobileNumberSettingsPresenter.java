package monsterstack.io.partner.settings.presenter;

import android.content.Context;
import android.content.res.Configuration;
import android.inputmethodservice.KeyboardView;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import java.util.Map;
import java.util.Optional;

import butterknife.BindView;
import monsterstack.io.api.UserSessionManager;
import monsterstack.io.api.resources.AuthenticatedUser;
import monsterstack.io.partner.common.HasProgressBarSupport;
import monsterstack.io.partner.common.presenter.PresenterAdapter;
import monsterstack.io.partner.R;

import static android.view.View.GONE;

public class MobileNumberSettingsPresenter extends PresenterAdapter implements HasProgressBarSupport {
    @BindView(R.id.mobileEdit)
    EditText editText;

    @BindView(R.id.keyboard)
    KeyboardView keyboardView;

    @BindView(R.id.progressbar)
    ProgressBar progressBar;

    private Context context;

    public MobileNumberSettingsPresenter(Context context) {
        this.context = context;
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
    public void present(Optional<Map> metadata) {
        editText.setSelection(editText.getText().length());
        editText.setRawInputType(Configuration.KEYBOARD_12KEY);

        keyboardView.setActivated(true);
        keyboardView.setEnabled(true);

        UserSessionManager userSessionManager = new UserSessionManager(this.context);
        AuthenticatedUser user = userSessionManager.getUserDetails();

        if(null != user.getPhoneNumber()) {
            editText.setText(user.getPhoneNumber());
        }
    }

    public String getCapturedPhoneNumber() {
        return editText.getText().toString();
    }
}
