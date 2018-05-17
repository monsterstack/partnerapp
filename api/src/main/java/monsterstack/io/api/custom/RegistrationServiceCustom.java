package monsterstack.io.api.custom;

import monsterstack.io.api.RegistrationService;
import monsterstack.io.api.listeners.OnResponseListener;
import monsterstack.io.api.resources.AuthenticatedUser;
import monsterstack.io.api.resources.HttpError;
import monsterstack.io.api.resources.Registration;

public interface RegistrationServiceCustom extends RegistrationService {
    static final String BEARER = "Bearer";

    void registerUser(Registration registration, OnResponseListener<AuthenticatedUser, HttpError> listener);
}
