package monsterstack.io.partner.registration.control;

import android.text.TextUtils;
import android.util.Patterns;
import android.view.MenuItem;

import monsterstack.io.partner.common.Control;

public interface RegistrationEmailCaptureControl extends Control {
    void onNext(MenuItem menuItem);

    default boolean isValidEmail(String email) {
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }
}
