package monsterstack.io.partner.challenge;

import android.content.Intent;
import android.os.Bundle;

import java.io.Serializable;

import monsterstack.io.api.ServiceLocator;
import monsterstack.io.api.UserSessionManager;
import monsterstack.io.api.custom.ChallengeServiceCustom;
import monsterstack.io.api.custom.RegistrationServiceCustom;
import monsterstack.io.api.listeners.OnResponseListener;
import monsterstack.io.api.resources.AuthenticatedUser;
import monsterstack.io.api.resources.Challenge;
import monsterstack.io.api.resources.HttpError;
import monsterstack.io.api.resources.Registration;
import monsterstack.io.partner.R;
import monsterstack.io.partner.utils.NavigationUtils;

public class RegistrationChallengeVerificationActivity extends ChallengeVerificationActivity {

    @Override
    public void onVerify() {
        presenter.showProgressBar();

        final ServiceLocator serviceLocator = getServiceLocator();
        ChallengeServiceCustom challengeServiceCustom = serviceLocator.getChallengeService();
        final RegistrationServiceCustom registrationServiceCustom = serviceLocator.getRegistrationService();

        String code = presenter.getCapturedCode();

        final UserSessionManager sessionManager = new UserSessionManager(getApplicationContext());

        challengeServiceCustom.verifyChallengeCode(code, new OnResponseListener<Challenge, HttpError>() {
            @Override
            public void onResponse(Challenge challenge, HttpError httpError) {
                if (null != challenge) {
                    Serializable registration = getIntent().getSerializableExtra("registration");
                    if (null != registration) {
                        Registration myRegistration = (Registration) registration;
                        myRegistration.getUser().setPhoneNumber(challenge.getPhoneNumber());
                        registerUser(sessionManager, registrationServiceCustom, myRegistration);
                    } else {
                        showError(getResources().getString(getActionTitle()), "Registration Missing!");
                        presenter.hideProgressBar();
                    }
                } else {
                    if (httpError.getStatusCode() == 404) {
                        showHttpError(getResources().getString(getActionTitle()),
                                getResources().getString(R.string.verification_code_not_found),
                                httpError);
                    } else {
                        showHttpError(getResources().getString(getActionTitle()),
                                httpError);
                    }

                    presenter.hideProgressBar();
                }
            }
        });
    }

    private void registerUser(final UserSessionManager sessionManager,
                              RegistrationServiceCustom registrationServiceCustom, Registration myRegistration) {
        registrationServiceCustom.registerUser(myRegistration, new OnResponseListener<AuthenticatedUser, HttpError>() {
            @Override
            public void onResponse(AuthenticatedUser user, HttpError httpError) {
                if (null != user) {
                    /* Add to session */
                    sessionManager.createUserSession(user);

                    /* For now set PIN 0000 */
                    sessionManager.createUserPin("0000");

                    Bundle bundle = NavigationUtils.enterStageRightBundle(getApplicationContext());
                    Intent intent = new Intent(RegistrationChallengeVerificationActivity.this.getApplicationContext(), RegistrationPinCaptureActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent, bundle);
                    presenter.hideProgressBar();
                } else {
                    presenter.hideProgressBar();
                    showHttpError(getResources().getString(getActionTitle()), httpError);
                }
            }
        });
    }
}
