package monsterstack.io.partner.data;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import org.robolectric.RuntimeEnvironment;

import monsterstack.io.api.resources.AuthenticatedUser;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.robolectric.Shadows.shadowOf;

public interface Persona {
    String getFullName();
    String getFirstName();
    String getLastName();
    String getPhoneNumber();
    String getEmailAddress();
    AuthenticatedUser getAuthenticatedUser();

    Persona authenticate(Context context);

    default void verifyNavigatedTo(Context from, Class<? extends Activity> to) {
        Intent expectedIntent = new Intent(from, to);
        Intent actual = shadowOf(RuntimeEnvironment.application).getNextStartedActivity();
        assertNotNull(actual);
        assertEquals(expectedIntent.getComponent(), actual.getComponent());
    }
}
