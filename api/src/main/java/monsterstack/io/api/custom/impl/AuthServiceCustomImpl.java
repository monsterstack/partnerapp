package monsterstack.io.api.custom.impl;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import monsterstack.io.api.AuthService;
import monsterstack.io.api.UserService;
import monsterstack.io.api.custom.AuthServiceCustom;
import monsterstack.io.api.listeners.OnResponseListener;
import monsterstack.io.api.resources.AuthVerification;
import monsterstack.io.api.resources.AuthenticatedUser;
import monsterstack.io.api.resources.Challenge;
import monsterstack.io.api.resources.HttpError;
import monsterstack.io.api.resources.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AuthServiceCustomImpl implements AuthServiceCustom, AuthService {
    private static final String API = "AuthServiceCustomImpl-API";

    private AuthService authService;
    private UserService userService;

    public AuthServiceCustomImpl(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @Override
    public void requestAccess(Challenge challenge, final OnResponseListener<Void, HttpError> listener) {
        Call<Void> challengeCall = authService.requestAccess(challenge);
        challengeCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    listener.onResponse(null, null);
                } else {
                    listener.onResponse(null, new HttpError(response.code(),
                            response.message()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                listener.onResponse(null, new HttpError(500, t.getMessage()));
            }
        });
    }

    @Override
    public void verifyAuthByCode(String code, final OnResponseListener<AuthenticatedUser, HttpError> listener) {
        Call<AuthVerification> authVerificationCall = authService.verifyAuthByCode(code);
        authVerificationCall.enqueue(new Callback<AuthVerification>() {
            @Override
            public void onResponse(@NonNull Call<AuthVerification> call, @NonNull Response<AuthVerification> response) {
                if(response.isSuccessful()) {
                    AuthVerification myAuthVerification = response.body();
                    /* Do Firebase sign-in with signin_token */
                    Task<AuthResult> task = FirebaseAuth.getInstance()
                            .signInWithCustomToken(null == myAuthVerification ? "" : myAuthVerification.getSignInToken());
                    task.addOnSuccessListener(authResultOnSuccessListener(myAuthVerification, listener));
                } else {
                    listener.onResponse(null, new HttpError(response));
                }
            }

            @Override
            public void onFailure(@NonNull Call<AuthVerification> call, @NonNull Throwable t) {
                listener.onResponse(null, new HttpError(500, t.getMessage()));
            }
        });
    }

    @Override
    public Call<Void> requestAccess(Challenge challenge) {
        return authService.requestAccess(challenge);
    }

    @Override
    public Call<AuthVerification> verifyAuthByCode(String code) {
        return authService.verifyAuthByCode(code);
    }

    private OnSuccessListener<AuthResult> authResultOnSuccessListener(final AuthVerification authVerification,
                                                                      final OnResponseListener<AuthenticatedUser, HttpError> listener) {
        return new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                FirebaseUser user = authResult.getUser();
                idToken(user, new OnSuccessListener<GetTokenResult>() {
                    @Override
                    public void onSuccess(GetTokenResult getTokenResult) {
                        Log.d(API, "Getting user for registration");
                        authVerification.setIdToken(getTokenResult.getToken());
                        finalizeAuth(authVerification, listener);
                    }
                });
            }
        };
    }

    private void idToken(FirebaseUser user, OnSuccessListener<GetTokenResult> listener) {
        Task<GetTokenResult> getTokenTask = user.getIdToken(false);
        getTokenTask.addOnSuccessListener(listener);
    }

    private void finalizeAuth(final AuthVerification authVerification, final OnResponseListener<AuthenticatedUser, HttpError> listener) {
        // Not sure why this seems to be invoked more than once.
        if(null != authVerification.getUserId()) {
            String bearerToken = BEARER + " " + authVerification.getIdToken();
            Call<User> signedInUserCall = userService.getUser(bearerToken, authVerification.getUserId());

            signedInUserCall.enqueue(new Callback<User>() {

                @Override
                public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                    if (response.isSuccessful()) {
                        AuthenticatedUser user = new AuthenticatedUser(response.body());
                        user.setIdToken(authVerification.getIdToken());
                        listener.onResponse(user, null);
                    } else {
                        listener.onResponse(null,
                                new HttpError(response.code(), response.message()));
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
