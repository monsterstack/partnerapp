package monsterstack.io.partner.settings;

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
import monsterstack.io.api.custom.UserServiceCustom;
import monsterstack.io.api.listeners.OnResponseListener;
import monsterstack.io.api.resources.AuthenticatedUser;
import monsterstack.io.api.resources.HttpError;
import monsterstack.io.api.resources.User;
import monsterstack.io.partner.R;

import static android.view.View.GONE;

public class EmailSettingsActivity extends DetailSettingsActivity {
    @BindView(R.id.emailEdit)
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

        keyboardView.setActivated(true);
        keyboardView.setEnabled(true);

        UserSessionManager userSessionManager = new UserSessionManager(this);
        AuthenticatedUser user = userSessionManager.getUserDetails();

        /* clear it */
        editText.setText(null);

        if(null != user) {
            editText.setText(user.getEmailAddress());
        }
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
    public int getContentView() {
        return R.layout.email_settings;
    }

    @Override
    public void setUpTransitions() {

    }

    @Override
    public int getActionTitle() {
        return R.string.detail_settings_email;
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void onUpdate() {
        updateEmailAddress();
    }

    private void updateEmailAddress() {
        progressBar.setVisibility(View.VISIBLE);
        final UserSessionManager sessionManager = new UserSessionManager(getApplicationContext());
        final AuthenticatedUser authenticatedUser = sessionManager.getUserDetails();
        ServiceLocator serviceLocator = ServiceLocator.getInstance(this);

        UserServiceCustom userServiceCustom = serviceLocator.getUserService();
        final User userToUpdate = User.from(authenticatedUser);

        String newEmailAddress = editText.getText().toString();
        userToUpdate.setEmailAddress(newEmailAddress);

        userServiceCustom.updateUser(userToUpdate.getId(), userToUpdate, new OnResponseListener<User, HttpError>() {
            @Override
            public void onResponse(User user, HttpError httpError) {
                if (null != user) {
                    authenticatedUser.setEmailAddress(user.getEmailAddress());
                    sessionManager.createUserSession(authenticatedUser);
                    // then if alls good
                    progressBar.setVisibility(GONE);
                    finish();
                } else {
                    progressBar.setVisibility(GONE);
                    showHttpError(getResources().getString(getActionTitle()), httpError);
                }
            }
        });
    }
}
