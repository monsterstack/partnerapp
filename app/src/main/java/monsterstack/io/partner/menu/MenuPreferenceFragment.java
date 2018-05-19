package monsterstack.io.partner.menu;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import monsterstack.io.api.ServiceLocator;
import monsterstack.io.api.UserSessionManager;
import monsterstack.io.api.custom.UserServiceCustom;
import monsterstack.io.api.listeners.OnResponseListener;
import monsterstack.io.api.resources.AuthenticatedUser;
import monsterstack.io.api.resources.HttpError;
import monsterstack.io.api.resources.User;
import monsterstack.io.partner.R;
import monsterstack.io.partner.challenge.ChangePinCaptureActivity;
import monsterstack.io.partner.settings.AboutUsActivity;
import monsterstack.io.partner.settings.EmailSettingsActivity;
import monsterstack.io.partner.settings.LocalCurrencySettingsActivity;
import monsterstack.io.partner.settings.MobileNumberSettingsActivity;
import monsterstack.io.partner.settings.PrivacyPolicyActivity;
import monsterstack.io.partner.settings.TermsOfServiceActivity;
import monsterstack.io.partner.settings.TwoStepVerificationSettingsActivity;
import monsterstack.io.partner.settings.WalletSettingsActivity;
import monsterstack.io.partner.utils.NavigationUtils;

import static android.content.Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS;

public class MenuPreferenceFragment extends PreferenceFragment {
    private Map<CharSequence, Class> namespace;

    @Override
    public void onCreate(final Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getPreferenceManager().setSharedPreferencesName(UserSessionManager.PREF_NAME);
        addPreferencesFromResource(R.xml.preferences);
        init();

        ServiceLocator serviceLocator = ServiceLocator.getInstance(getContext());
        final UserServiceCustom userServiceCustom = serviceLocator.getUserService();
        final UserSessionManager userSessionManager = new UserSessionManager(getContext());
        final AuthenticatedUser authenticatedUser = userSessionManager.getUserDetails();

        // show the current value in the settings screen
        for (int i = 0; i < getPreferenceScreen().getPreferenceCount(); i++) {
            Preference preference = getPreferenceScreen().getPreference(i);

            if(preference instanceof PreferenceCategory) {
                PreferenceCategory category = (PreferenceCategory)preference;

                int count = category.getPreferenceCount();

                for (int j = 0; j < count; j++) {
                    Preference subPreference = category.getPreference(j);
                    final CharSequence title = subPreference.getTitle();

                    Object value = userSessionManager.get(subPreference.getKey());
                    if(null != value)
                        subPreference.setDefaultValue(value);

                    try {
                        final Class target = lookupByKey(title);
                        if (null != target) {
                            subPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                                @Override
                                public boolean onPreferenceClick(Preference preference) {
                                    return navigate(getPreferenceScreen().getContext(), target);
                                }
                            });
                        } else {
                            subPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                                @Override
                                public boolean onPreferenceChange(Preference preference, Object newValue) {
                                    if (preference.getKey() == null) {
                                        return true;
                                    }

                                    if(preference.getKey().equals(UserSessionManager.USER_EMAIL_NOTIFY)) {
                                        authenticatedUser.setEmailNotifications((Boolean)newValue);
                                    } else if(preference.getKey().equals(UserSessionManager.USER_SMS_NOTIFY)) {
                                        authenticatedUser.setSmsNotifications((Boolean)newValue);
                                    }

                                    userSessionManager.createUserSession(authenticatedUser);

                                    userServiceCustom.updateUser(authenticatedUser.getId(), userSessionManager.getUserDetails(),
                                            new OnResponseListener<User, HttpError>() {
                                        @Override
                                        public void onResponse(User user, HttpError httpError) {
                                            if (null != user) {
                                                Log.d("MenuPreferences", "User updated");
                                            }
                                        }
                                    });
                                    return true;
                                }
                            });
                        }
                    } catch(Resources.NotFoundException notFound) {
                        ;
                    }
                }
            }
        }
    }

    private boolean navigate(Context context, Class destination) {
        Bundle bundle = NavigationUtils.enterStageRightBundle(context);
        Intent intent = new Intent(context, destination);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        intent.putExtra("source", this.getClass().getCanonicalName());
        startActivity(intent, bundle);

        return false;
    }

    private Class lookupByKey(CharSequence key) {
        return namespace.get(key);
    }

    private void init() {
        namespace = new HashMap<>();
        namespace.put(getString(R.string.detail_settings_pin), ChangePinCaptureActivity.class);
        namespace.put(getString(R.string.detail_settings_email), EmailSettingsActivity.class);
        namespace.put(getString(R.string.detail_settings_mobileNumber), MobileNumberSettingsActivity.class);
        namespace.put(getString(R.string.detail_settings_local_currency), LocalCurrencySettingsActivity.class);
        namespace.put(getString(R.string.detail_settings_wallet_id), WalletSettingsActivity.class);
        namespace.put(getString(R.string.detail_settings_two_step_verify), TwoStepVerificationSettingsActivity.class);

        namespace.put(getString(R.string.about_about_us), AboutUsActivity.class);
        namespace.put(getString(R.string.about_terms_of_service), TermsOfServiceActivity.class);
        namespace.put(getString(R.string.about_privacy_policy), PrivacyPolicyActivity.class);
    }
}
