package monsterstack.io.api.custom.impl;

import android.support.annotation.NonNull;

import java.util.List;

import monsterstack.io.api.UserService;
import monsterstack.io.api.custom.UserServiceCustom;
import monsterstack.io.api.listeners.OnResponseListener;
import monsterstack.io.api.resources.HttpError;
import monsterstack.io.api.resources.Identifier;
import monsterstack.io.api.resources.User;
import monsterstack.io.api.resources.Wallet;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserServiceCustomImpl implements UserServiceCustom {
    private UserService userService;
    public UserServiceCustomImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void updateUser(String userId, final User userToUpdate, final OnResponseListener<User, HttpError> listener) {
        Call<Identifier> userUpdateCall = userService.updateUser(userId, userToUpdate);
        userUpdateCall.enqueue(new Callback<Identifier>() {
            @Override
            public void onResponse(@NonNull Call<Identifier> call, @NonNull Response<Identifier> response) {
                if(response.isSuccessful()) {
                    listener.onResponse(userToUpdate, null);
                } else {
                    listener.onResponse(null, new HttpError(response));
                }
            }

            @Override
            public void onFailure(@NonNull Call<Identifier> call, @NonNull Throwable t) {
                listener.onResponse(null, new HttpError(500, t.getMessage()));
            }
        });
    }

    @Override
    public Call<Identifier> createUser(User user) {
        return userService.createUser(user);
    }

    @Override
    public Call<Identifier> updateUser(String userId, User user) {
        return userService.updateUser(userId, user);
    }

    @Override
    public Call<Void> deleteUser(String userId) {
        return userService.deleteUser(userId);
    }

    @Override
    public Call<User> getUser(String bearerToken, String userId) {
        return userService.getUser(bearerToken, userId);
    }

    @Override
    public Call<User> getUser(String userId) {
        return userService.getUser(userId);
    }

    @Override
    public Call<List<User>> getUsers(String startAt, Integer limit) {
        return userService.getUsers(startAt, limit);
    }

    @Override
    public Call<Identifier> attachWalletToUser(String userId, Wallet wallet) {
        return userService.attachWalletToUser(userId, wallet);
    }

    @Override
    public Call<Void> detachWalletFromUser(String userId) {
        return userService.detachWalletFromUser(userId);
    }
}
