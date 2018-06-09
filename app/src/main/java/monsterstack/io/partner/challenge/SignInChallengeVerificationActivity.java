package monsterstack.io.partner.challenge;

import android.content.Intent;
import android.os.Bundle;

import monsterstack.io.api.ServiceLocator;
import monsterstack.io.api.UserSessionManager;
import monsterstack.io.api.custom.AuthServiceCustom;
import monsterstack.io.api.listeners.OnResponseListener;
import monsterstack.io.api.resources.AuthenticatedUser;
import monsterstack.io.api.resources.HttpError;
import monsterstack.io.partner.MainActivity;
import monsterstack.io.partner.R;
import monsterstack.io.partner.services.MessagingService;
import monsterstack.io.partner.utils.NavigationUtils;

public class SignInChallengeVerificationActivity extends ChallengeVerificationActivity {

    @Override
    public void onVerify() {
        presenter.showProgressBar();

        ServiceLocator serviceLocator = getServiceLocator();
        final AuthServiceCustom authServiceCustom = serviceLocator.getAuthService();

        String code = presenter.getCapturedCode();

        final UserSessionManager sessionManager = new UserSessionManager(getApplicationContext());

        authServiceCustom.verifyAuthByCode(code, new OnResponseListener<AuthenticatedUser, HttpError>() {
            @Override
            public void onResponse(AuthenticatedUser user, HttpError httpError) {
                if(null != user) {
                    /* Add to session */
                    sessionManager.createUserSession(user);
                    MessagingService.initialize(getApplicationContext());

                    String pin = sessionManager.getUserPin();

                    if (null != pin) {
                        Bundle bundle = NavigationUtils.enterStageRightBundle(getApplicationContext());
                        Intent intent = new Intent(SignInChallengeVerificationActivity.this.getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent, bundle);
                    } else {
                        Bundle bundle = NavigationUtils.enterStageRightBundle(getApplicationContext());
                        Intent intent = new Intent(SignInChallengeVerificationActivity.this.getApplicationContext(), RegistrationPinCaptureActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent, bundle);
                    }
                    presenter.hideProgressBar();
                } else {
                    if (httpError.getStatusCode() == 404) {
                        showHttpError(getResources().getString(getActionTitle()),
                                getResources().getString(R.string.verification_code_not_found),
                                httpError);
                    } else if (httpError.getStatusCode() == 403) {
                        showHttpError(getResources().getString(getActionTitle()),
                                "Sign In Failed",
                                httpError);
                    } else {
                        showHttpError(getResources().getString(getActionTitle()),
                                httpError);
                    }

                    presenter.showProgressBar();
                }
            }
        });

    }
}
