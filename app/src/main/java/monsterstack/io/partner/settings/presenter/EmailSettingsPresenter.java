package monsterstack.io.partner.settings.presenter;

import android.app.Activity;
import android.content.Context;
import android.inputmethodservice.KeyboardView;
import android.view.Menu;
import android.view.MenuItem;
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
import monsterstack.io.partner.settings.EmailSettingsActivity;
import monsterstack.io.partner.settings.control.EmailSettingsControl;

import static android.view.View.GONE;

public class EmailSettingsPresenter extends PresenterAdapter implements HasProgressBarSupport {
    @BindView(R.id.emailEdit)
    EditText editText;
    @BindView(R.id.keyboard)
    KeyboardView keyboardView;

    @BindView(R.id.progressbar)
    ProgressBar progressBar;

    private EmailSettingsControl control;

    public EmailSettingsPresenter(EmailSettingsControl control) {
        this.control = control;
    }

    public String getCapturedEmailAddress() {
        return editText.getText().toString();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        ((Activity)control.getContext()).getMenuInflater().inflate(R.menu.update_action, menu);

        // Damn Coupling..
        menu.findItem(R.id.update_button).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                control.onUpdate();
                return false;
            }
        });
        return true;
    }

    @Override
    public void present(Optional<Map> metadata) {
        editText.setSelection(editText.getText().length());

        keyboardView.setActivated(true);
        keyboardView.setEnabled(true);

        UserSessionManager userSessionManager = new UserSessionManager(control.getContext());
        AuthenticatedUser user = userSessionManager.getUserDetails();

        /* clear it */
        editText.setText(null);

        if(null != user) {
            editText.setText(user.getEmailAddress());
        }
    }
}
