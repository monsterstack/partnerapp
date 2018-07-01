package monsterstack.io.partner.services;

import android.content.Context;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import javax.inject.Singleton;

import monsterstack.io.api.ServiceLocator;
import monsterstack.io.api.UserSessionManager;
import monsterstack.io.api.custom.UserServiceCustom;
import monsterstack.io.api.resources.AuthenticatedUser;

@Singleton
public class PartnerFirebaseInstanceIdService extends FirebaseInstanceIdService {
    private static final String TAG = "FCM";

    private ServiceLocator serviceLocator;

    public PartnerFirebaseInstanceIdService(ServiceLocator serviceLocator) {
        this.serviceLocator = serviceLocator;
    }

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken, getApplicationContext());
    }


    public void sendRegistrationToServer(String refreshToken, Context context) {

        final UserSessionManager userSessionManager = new UserSessionManager(context);
        AuthenticatedUser authenticatedUser = userSessionManager.getUserDetails();

        if (null != authenticatedUser.getId()) {
            // Set device id
            authenticatedUser.setPushRegistrationToken(refreshToken);

            UserServiceCustom userServiceCustom = serviceLocator.getUserService();
            userServiceCustom.updateUser(authenticatedUser.getId(), authenticatedUser, (user, httpError) -> {
                if (null != user) {
                    userSessionManager.createUserSession(new AuthenticatedUser(user));
                }
            });
        }
    }
}
