package monsterstack.io.partner.settings.presenter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Map;
import java.util.Optional;

import butterknife.BindView;
import butterknife.OnClick;
import monsterstack.io.api.ServiceLocator;
import monsterstack.io.api.UserSessionManager;
import monsterstack.io.api.custom.UserServiceCustom;
import monsterstack.io.api.listeners.OnResponseListener;
import monsterstack.io.api.resources.AuthenticatedUser;
import monsterstack.io.api.resources.HttpError;
import monsterstack.io.api.resources.User;
import monsterstack.io.partner.common.HasProgressBarSupport;
import monsterstack.io.partner.common.presenter.PresenterAdapter;
import monsterstack.io.partner.R;
import monsterstack.io.partner.settings.TwoStepVerificationSettingsActivity;

import static android.view.View.GONE;

public class TwoStepVerificationSettingsPresenter extends PresenterAdapter implements HasProgressBarSupport {
    @BindView(R.id.two_step_verify_enable_button)
    Button enableTwoFactorButton;

    @BindView(R.id.two_step_verify_description)
    TextView description;

    @BindView(R.id.progressbar)
    ProgressBar progressBar;

    private Context context;

    public TwoStepVerificationSettingsPresenter(Context context) {
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
        UserSessionManager userSessionManager = new UserSessionManager(this.context);
        AuthenticatedUser user = userSessionManager.getUserDetails();

        if(user.getTwoFactorAuth()) {
            enableTwoFactorButton.setText("Disable Two-Factor Auth");
            description.setText(R.string.disable_two_factor_description);
        } else {
            enableTwoFactorButton.setText("Enable Two-Factor Auth");
            description.setText(R.string.enable_two_factor_description);
        }
    }

    @OnClick(R.id.two_step_verify_enable_button)
    public void onEnable(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(((TwoStepVerificationSettingsActivity)context).getActivity());

        ServiceLocator serviceLocator = ServiceLocator.getInstance(context);
        final UserServiceCustom userService = serviceLocator.getUserService();

        final UserSessionManager userSessionManager = new UserSessionManager(context);
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

                            ((TwoStepVerificationSettingsActivity)context).finish();
                        } else {
                            progressBar.setVisibility(View.GONE);

                            ((TwoStepVerificationSettingsActivity)context).showHttpError(
                                    ((TwoStepVerificationSettingsActivity)context).getResources().getString(
                                            ((TwoStepVerificationSettingsActivity)context).getActionTitle()), httpError);
                        }
                    }
                });

            }
        };

        DialogInterface.OnClickListener no = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                ((TwoStepVerificationSettingsActivity)context).finish();
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