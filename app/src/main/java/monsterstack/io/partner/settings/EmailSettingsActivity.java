package monsterstack.io.partner.settings;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;

import java.util.Map;
import java.util.Optional;

import butterknife.ButterKnife;
import monsterstack.io.api.ServiceLocator;
import monsterstack.io.api.UserSessionManager;
import monsterstack.io.api.custom.UserServiceCustom;
import monsterstack.io.api.listeners.OnResponseListener;
import monsterstack.io.api.resources.AuthenticatedUser;
import monsterstack.io.api.resources.HttpError;
import monsterstack.io.api.resources.User;
import monsterstack.io.partner.R;
import monsterstack.io.partner.settings.control.EmailSettingsControl;
import monsterstack.io.partner.settings.presenter.EmailSettingsPresenter;

public class EmailSettingsActivity extends DetailSettingsActivity implements EmailSettingsControl {
    protected EmailSettingsPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new EmailSettingsPresenter(this);
        ButterKnife.bind(presenter, this);

        presenter.present(Optional.<Map>empty());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return presenter.onCreateOptionsMenu(menu);
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

    @Override
    public void onUpdate() {
        updateEmailAddress();
    }

    @Override
    public Context getContext() { return this; }

    private void updateEmailAddress() {
        presenter.showProgressBar();
        final UserSessionManager sessionManager = new UserSessionManager(getApplicationContext());
        final AuthenticatedUser authenticatedUser = sessionManager.getUserDetails();
        ServiceLocator serviceLocator = getServiceLocator();

        UserServiceCustom userServiceCustom = serviceLocator.getUserService();
        final User userToUpdate = User.from(authenticatedUser);

        String newEmailAddress = presenter.getCapturedEmailAddress();
        userToUpdate.setEmailAddress(newEmailAddress);

        userServiceCustom.updateUser(userToUpdate.getId(), userToUpdate, new OnResponseListener<User, HttpError>() {
            @Override
            public void onResponse(User user, HttpError httpError) {
                if (null != user) {
                    authenticatedUser.setEmailAddress(user.getEmailAddress());
                    sessionManager.createUserSession(authenticatedUser);
                    // then if alls good
                    presenter.hideProgressBar();
                    finish();
                } else {
                    presenter.hideProgressBar();
                    showHttpError(getResources().getString(getActionTitle()), httpError);
                }
            }
        });
    }
}
