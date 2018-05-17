package monsterstack.io.partner.settings;

import android.app.ActivityOptions;
import android.content.Intent;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import monsterstack.io.api.ServiceLocator;
import monsterstack.io.api.UserSessionManager;
import monsterstack.io.api.custom.UserServiceCustom;
import monsterstack.io.api.listeners.OnResponseListener;
import monsterstack.io.api.resources.AuthenticatedUser;
import monsterstack.io.api.resources.HttpError;
import monsterstack.io.api.resources.User;
import monsterstack.io.partner.R;
import monsterstack.io.partner.menu.SettingsActivity;

public class EmailSettingsActivity extends DetailSettingsActivity {
    @BindView(R.id.emailEdit)
    EditText editText;
    @BindView(R.id.keyboard)
    KeyboardView keyboardView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        editText.setSelection(editText.getText().length());

        keyboardView.setActivated(true);
        keyboardView.setEnabled(true);
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

    @OnClick(R.id.emailUpdateButton)
    public void performUpdate(View view) {
        updateEmailAddress();
    }

    private void updateEmailAddress() {

        final UserSessionManager sessionManager = new UserSessionManager(getApplicationContext());
        final AuthenticatedUser authenticatedUser = sessionManager.getUserDetails();
        ServiceLocator serviceLocator = ServiceLocator.getInstance(this);

        UserServiceCustom userServiceCustom = serviceLocator.getUserService();
        User userToUpdate = User.from(authenticatedUser);

        String newEmailAddress = editText.getText().toString();
        userToUpdate.setEmailAddress(newEmailAddress);

        userServiceCustom.updateUser(userToUpdate.getId(), userToUpdate, new OnResponseListener<User, HttpError>() {
            @Override
            public void onResponse(User user, HttpError httpError) {
                if (null != user) {
                    // then if alls good
                    Bundle bundle = ActivityOptions.makeCustomAnimation(getApplicationContext(),
                            R.anim.back_slide_right, R.anim.back_slide_left).toBundle();
                    startActivity(new Intent(getApplicationContext(), SettingsActivity.class), bundle);

                } else {
                    showHttpError(getResources().getString(getActionTitle()), httpError);
                }
            }
        });
    }
}
