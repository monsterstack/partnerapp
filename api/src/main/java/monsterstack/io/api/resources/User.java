package monsterstack.io.api.resources;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User extends MinimalUser {
    @JsonProperty("email_address")
    private String emailAddress;
    @JsonProperty("avatar_url")
    private String avatarUrl;

    @JsonProperty("sms_notifications")
    private Boolean smsNotifications;
    @JsonProperty("email_notifications")
    private Boolean emailNotifications;

    @JsonProperty("two_factor_auth")
    private Boolean twoFactorAuth;

    @JsonProperty("push_registration_token")
    private String pushRegistrationToken;

    @JsonProperty("wallet")
    private Wallet wallet;

    public User() {
        this.smsNotifications = false;
        this.emailNotifications = false;
        this.twoFactorAuth = false;
    }

    public static User from(AuthenticatedUser authenticatedUser) {
        User user = new User();
        user.setId(authenticatedUser.getId());
        user.setAvatarUrl(authenticatedUser.getAvatarUrl());
        user.setPhoneNumber(authenticatedUser.getPhoneNumber());
        user.setLastName(authenticatedUser.getLastName());
        user.setFirstName(authenticatedUser.getFirstName());
        user.setEmailAddress(authenticatedUser.getEmailAddress());
        user.setWallet(authenticatedUser.getWallet());

        user.setEmailNotifications(authenticatedUser.getEmailNotifications());
        user.setSmsNotifications(authenticatedUser.getSmsNotifications());
        user.setTwoFactorAuth(authenticatedUser.getTwoFactorAuth());

        user.setPushRegistrationToken(authenticatedUser.getPushRegistrationToken());

        return user;
    }
}
