package monsterstack.io.api.custom.impl;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import monsterstack.io.api.RegistrationService;
import monsterstack.io.api.UserService;
import monsterstack.io.api.custom.RegistrationServiceCustom;
import monsterstack.io.api.listeners.OnResponseListener;
import monsterstack.io.api.resources.AuthenticatedUser;
import monsterstack.io.api.resources.HttpError;
import monsterstack.io.api.resources.Registration;
import monsterstack.io.api.resources.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationServiceCustomImpl implements RegistrationServiceCustom, RegistrationService {
    private static final String API = "RegistrationServiceCustomImpl-API";
    private RegistrationService registrationService;
    private UserService userService;

    public RegistrationServiceCustomImpl(RegistrationService registrationService,
                                         UserService userService) {
        this.registrationService = registrationService;
        this.userService = userService;
    }

    @Override
    public Call<Registration> register(Registration registration) {
        return registrationService.register(registration);
    }

    @Override
    public void registerUser(final Registration registration,
                                      final OnResponseListener<AuthenticatedUser, HttpError> listener)  {
        Call<Registration> registrationCall = this.registrationService.register(registration);

        registrationCall.enqueue(new Callback<Registration>() {

            @Override
            public void onResponse(@NonNull Call<Registration> call, @NonNull Response<Registration> response) {
                if (response.isSuccessful()) {
                    Registration myRegistration = response.body();

                    /* Do Firebase sign-in with signin_token */
                    Task<AuthResult> task = FirebaseAuth.getInstance()
                            .signInWithCustomToken(myRegistration.getSignInToken());
                    task.addOnSuccessListener(authResultOnSuccessListener(myRegistration, listener));
                } else {
                    listener.onResponse(null, new HttpError(response.code(),
                            response.message()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<Registration> call, @NonNull Throwable t) {
                // Shouldn't happen..
                listener.onResponse(null, new HttpError(500, t.getMessage()));

            }
        });

    }


    private OnSuccessListener<AuthResult> authResultOnSuccessListener(final Registration registration,
                                                                      final OnResponseListener<AuthenticatedUser, HttpError> listener) {
        return new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                FirebaseUser user = authResult.getUser();
                idToken(user, new OnSuccessListener<GetTokenResult>() {
                    @Override
                    public void onSuccess(GetTokenResult getTokenResult) {
                        registration.setIdToken(getTokenResult.getToken());
                        Log.d(API, "Getting user for registration");
                        finalizeRegistration(registration, listener);
                    }
                });
            }
        };
    }

    private void idToken(FirebaseUser user, OnSuccessListener<GetTokenResult> listener) {
        Task<GetTokenResult> getTokenTask = user.getIdToken(false);
        getTokenTask.addOnSuccessListener(listener);
    }

    private void finalizeRegistration(final Registration registration, final OnResponseListener<AuthenticatedUser, HttpError> listener) {
        // Not sure why this seems to be invoked more than once.
        if(null != registration.getUserId()) {
            String bearerToken = BEARER + " " + registration.getIdToken();
            Call<User> registeredUserCall = userService.getUser(bearerToken, registration.getUserId());

            registeredUserCall.enqueue(new Callback<User>() {

                @Override
                public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                    if (response.isSuccessful()) {
                        AuthenticatedUser authenticatedUser = new AuthenticatedUser(response.body());
                        authenticatedUser.setIdToken(registration.getIdToken());
                        listener.onResponse(authenticatedUser, null);
                    } else {
                        listener.onResponse(null,
                                new HttpError(response));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                    // Shouldn't happen..
                    listener.onResponse(null, new HttpError(500, t.getMessage()));
                }
            });
        }
    }
}
