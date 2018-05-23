package monsterstack.io.api.resources;

import android.support.annotation.NonNull;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthenticatedUser extends User {
    private String idToken;

    public AuthenticatedUser(@NonNull User user) {
        this.setId(user.getId());
        this.setAvatarUrl(user.getAvatarUrl());
        this.setEmailAddress(user.getEmailAddress());
        this.setPhoneNumber(user.getPhoneNumber());
        this.setWallet(user.getWallet());
        this.setFirstName(user.getFirstName());
        this.setLastName(user.getLastName());
        this.setEmailNotifications(user.getEmailNotifications());
        this.setSmsNotifications(user.getSmsNotifications());
        this.setTwoFactorAuth(user.getTwoFactorAuth());
        this.setPushRegistrationToken(user.getPushRegistrationToken());
    }

    public String getFullName() {
        return this.getFirstName() + " " + this.getLastName();
    }
}
