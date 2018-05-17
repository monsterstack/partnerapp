package monsterstack.io.api.custom;

import monsterstack.io.api.AuthService;
import monsterstack.io.api.listeners.OnResponseListener;
import monsterstack.io.api.resources.AuthenticatedUser;
import monsterstack.io.api.resources.Challenge;
import monsterstack.io.api.resources.HttpError;

public interface AuthServiceCustom extends AuthService {
    static final String BEARER = "Bearer";

    void requestAccess(Challenge challenge, OnResponseListener<Void, HttpError> listener);
    void verifyAuthByCode(String code, OnResponseListener<AuthenticatedUser, HttpError> listener);
}
