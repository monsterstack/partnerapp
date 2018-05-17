package monsterstack.io.api.custom;

import monsterstack.io.api.UserService;
import monsterstack.io.api.listeners.OnResponseListener;
import monsterstack.io.api.resources.HttpError;
import monsterstack.io.api.resources.User;

public interface UserServiceCustom extends UserService {
    void updateUser(String userId, User userToUpdate, OnResponseListener<User, HttpError> listener);
}
