package monsterstack.io.partner.settings;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

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

public class TwoStepVerificationSettingsActivity extends DetailSettingsActivity {
    @BindView(R.id.two_step_verify_enable_button)
    Button enableTwoFactorButton;

    @BindView(R.id.two_step_verify_description)
    TextView description;

    @BindView(R.id.progressbar)
    ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);

        UserSessionManager userSessionManager = new UserSessionManager(this);
        AuthenticatedUser user = userSessionManager.getUserDetails();

        if(user.getTwoFactorAuth()) {
            enableTwoFactorButton.setText("Disable Two-Factor Auth");
            description.setText(R.string.disable_two_factor_description);
        } else {
            enableTwoFactorButton.setText("Enable Two-Factor Auth");
            description.setText(R.string.enable_two_factor_description);
        }
    }

    @Override
    public int getContentView() {
        return R.layout.two_step_verification_settings;
    }

    @Override
    public void setUpTransitions() {

    }

    @Override
    public int getActionTitle() {
        return R.string.detail_settings_two_step_verify;
    }

    @OnClick(R.id.two_step_verify_enable_button)
    public void onEnable(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        ServiceLocator serviceLocator = ServiceLocator.getInstance(getApplicationContext());
        final UserServiceCustom userService = serviceLocator.getUserService();

        final UserSessionManager userSessionManager = new UserSessionManager(this);
        final AuthenticatedUser userToUpdate = userSessionManager.getUserDetails();
        if(userToUpdate.getTwoFactorAuth())
            userToUpdate.setTwoFactorAuth(false);
        else
            userToUpdate.setTwoFactorAuth(true);

        final String userId = userToUpdate.getId();

        DialogInterface.OnClickListener enabled = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                progressBar.setVisibility(View.VISIBLE);

                userService.updateUser(userId, userToUpdate, new OnResponseListener<User, HttpError>() {
                    @Override
                    public void onResponse(User user, HttpError httpError) {
                        if (null != user) {
                            userSessionManager.createUserSession(userToUpdate);
                            progressBar.setVisibility(View.GONE);

                            finish();
                        } else {
                            progressBar.setVisibility(View.GONE);

                            showHttpError(getResources().getString(getActionTitle()), httpError);
                        }
                    }
                });

            }
        };

        DialogInterface.OnClickListener no = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                finish();
            }
        };

        builder.setMessage(R.string.detail_settings_two_step_verify)
                .setTitle(R.string.detail_settings_two_step_verify)
                .setPositiveButton("Change", enabled)
                .setNegativeButton(R.string.dialogNo, no);

        AlertDialog dialog = builder.create();

        dialog.show();
    }
}
