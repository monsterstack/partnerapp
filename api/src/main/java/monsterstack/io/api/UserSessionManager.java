package monsterstack.io.api;

import android.content.Context;
import android.content.SharedPreferences;

import monsterstack.io.api.resources.AuthenticatedUser;
import monsterstack.io.api.service.RefreshTokenService;

public class UserSessionManager {
    private static final String USER_ID = "user.id";
    private static final String USER_FIRST_NAME = "user.firstName";
    private static final String USER_LAST_NAME = "user.lastName";
    private static final String USER_EMAIL = "user.email";
    private static final String USER_PHONE = "user.phoneNumber";
    private static final String USER_AVATAR = "user.avatarUrl";
    private static final String USER_TOKEN = "user.token";
    public static final String USER_SMS_NOTIFY = "user.sms.notifications";
    public static final String USER_EMAIL_NOTIFY = "user.email.notifications";
    private static final String USER_TWO_FACTOR = "user.two.factor";

    private static final String PIN = "pin";

    public static final String PREF_NAME = "PartnerPref";
    private static final Integer PREF_MODE = 0; // Private is 0

    private static final String IS_LOGIN = "IsLoggedIn";

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private Context context;

    public UserSessionManager(Context context) {
        this.context = context;
        this.preferences = context.getSharedPreferences(PREF_NAME, PREF_MODE);
        this.editor = this.preferences.edit();
    }

    public void createUserPin(String pin) {
        this.editor.putString(PIN, pin);
        this.editor.commit();
    }

    public String getUserPin() {
        return preferences.getString(PIN, null);
    }

    public void createUserSession(AuthenticatedUser user) {
        this.editor.putBoolean(IS_LOGIN, true);
        this.editor.putString(USER_ID, user.getId());
        this.editor.putString(USER_EMAIL, user.getEmailAddress());
        this.editor.putString(USER_FIRST_NAME, user.getFirstName());
        this.editor.putString(USER_LAST_NAME, user.getLastName());
        this.editor.putString(USER_PHONE, user.getPhoneNumber());
        this.editor.putString(USER_AVATAR, user.getAvatarUrl());
        this.editor.putString(USER_TOKEN, user.getIdToken());
        this.editor.putBoolean(USER_SMS_NOTIFY, user.getSmsNotifications());
        this.editor.putBoolean(USER_EMAIL_NOTIFY, user.getEmailNotifications());
        this.editor.putBoolean(USER_TWO_FACTOR, user.getTwoFactorAuth());

        this.editor.commit();

        RefreshTokenService.scheduleRefreshTokenCheck(context);
    }

    public Object get(String key) {
        return preferences.getAll().get(key);
    }

    public AuthenticatedUser getUserDetails() {
        AuthenticatedUser user = new AuthenticatedUser();
        user.setId(preferences.getString(USER_ID, null));
        user.setEmailAddress(preferences.getString(USER_EMAIL, null));
        user.setFirstName(preferences.getString(USER_FIRST_NAME, null));
        user.setLastName(preferences.getString(USER_LAST_NAME, null));
        user.setPhoneNumber(preferences.getString(USER_PHONE, null));
        user.setAvatarUrl(preferences.getString(USER_AVATAR, null));
        user.setIdToken(preferences.getString(USER_TOKEN, null));
        user.setSmsNotifications(preferences.getBoolean(USER_SMS_NOTIFY, false));
        user.setEmailNotifications(preferences.getBoolean(USER_EMAIL_NOTIFY, false));
        user.setTwoFactorAuth(preferences.getBoolean(USER_TWO_FACTOR, false));
        // return user
        return user;
    }

    public void checkLoginStatus(RedirectHandler handler) {
        if(!this.isLoggedIn()) {
            handler.go(this.context);
        }
    }

    public void logoutUser(RedirectHandler handler){
        // Clearing all data from Shared Preferences
        editor.putBoolean(IS_LOGIN, false);
        editor.commit();

        RefreshTokenService.cancelRefreshTokenCheck(context);

        handler.go(this.context);
    }

    private Boolean isLoggedIn() {
        return preferences.getBoolean(IS_LOGIN, false);
    }


}
