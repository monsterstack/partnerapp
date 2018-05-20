package monsterstack.io.partner.challenge;

import android.content.Intent;
import android.view.View;

import monsterstack.io.api.ServiceLocator;
import monsterstack.io.api.UserSessionManager;
import monsterstack.io.api.custom.ChallengeServiceCustom;
import monsterstack.io.api.custom.UserServiceCustom;
import monsterstack.io.api.listeners.OnResponseListener;
import monsterstack.io.api.resources.AuthenticatedUser;
import monsterstack.io.api.resources.Challenge;
import monsterstack.io.api.resources.HttpError;
import monsterstack.io.api.resources.User;
import monsterstack.io.partner.R;
import monsterstack.io.partner.menu.SettingsActivity;

import static android.view.View.GONE;

public class MobileNumberUpdateChallengeVerificationActivity extends ChallengeVerificationActivity {

    @Override
    public void onVerify() {
        progressBar.setVisibility(View.VISIBLE);

        ServiceLocator serviceLocator = getServiceLocator();
        final ChallengeServiceCustom challengeServiceCustom = serviceLocator.getChallengeService();
        final UserServiceCustom userServiceCustom = serviceLocator.getUserService();

        String code = editText.getEnteredText();

        final UserSessionManager sessionManager = new UserSessionManager(getApplicationContext());
        final AuthenticatedUser authenticatedUser = sessionManager.getUserDetails();
        challengeServiceCustom.verifyChallengeCode(code, verifyChallengeResponseListener(sessionManager,
                userServiceCustom, authenticatedUser));
    }

    private OnResponseListener<Challenge, HttpError> verifyChallengeResponseListener(final UserSessionManager sessionManager,
                                                                                     final UserServiceCustom userServiceCustom,
                                                                                     final AuthenticatedUser authenticatedUser) {
        return new OnResponseListener<Challenge, HttpError>() {
            @Override
            public void onResponse(Challenge challenge, HttpError httpError) {
                if(null != challenge && challenge.getValid()) {
                    String phoneNumber = challenge.getPhoneNumber();
                    User userToUpdate = User.from(authenticatedUser);

                    userToUpdate.setPhoneNumber(phoneNumber);

                    userServiceCustom.updateUser(userToUpdate.getId(), userToUpdate,
                            updateUserResponseListener(sessionManager, authenticatedUser));

                } else {
                    if (httpError.getStatusCode() == 404) {
                        showHttpError(getResources().getString(getActionTitle()),
                                getResources().getString(R.string.verification_code_not_found),
                                httpError);
                    } else {
                        showHttpError(getResources().getString(getActionTitle()),
                                httpError);
                    }

                    progressBar.setVisibility(GONE);
                }
            }
        };
    }

    private OnResponseListener<User, HttpError> updateUserResponseListener(final UserSessionManager sessionManager,
                                                                           final AuthenticatedUser authenticatedUser) {
        return new OnResponseListener<User, HttpError>() {
            @Override
            public void onResponse(User user, HttpError httpError) {
                if(null != user) {
                    /* Add to session */
                    authenticatedUser.setPhoneNumber(user.getPhoneNumber());
                    sessionManager.createUserSession(authenticatedUser);

                    Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    startActivity(intent, exitStageLeftBundle());
                    progressBar.setVisibility(GONE);
                } else {
                    showHttpError(getResources().getString(getActionTitle()), httpError);
                    progressBar.setVisibility(GONE);
                }
            }
        };
    }
}
