package monsterstack.io.partner;

import android.widget.Button;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import monsterstack.io.api.UserSessionManager;
import monsterstack.io.partner.challenge.SignInPhoneCaptureActivity;
import monsterstack.io.partner.challenge.SignInPinCaptureActivity;
import monsterstack.io.partner.data.Personas;
import monsterstack.io.partner.registration.RegistrationNameCaptureActivity;

@RunWith(RobolectricTestRunner.class)
@Config(
        sdk = 27,
        constants = BuildConfig.class)
public class LaunchActivityTest {

    private LaunchActivity launchActivity;

    private Personas.Molly molly;

    @Before
    public void setup() {
        launchActivity = Robolectric.buildActivity(LaunchActivity.class).create().visible().get();

        molly = new Personas().asMolly();
    }

    @Test
    public void givenSessionHasPin_whenClickingSignIn_expectNavigationToSignInPinCapture() {
        UserSessionManager sessionManager = new UserSessionManager(launchActivity);
        sessionManager.createUserPin("1111");

        Button signInButton = launchActivity.findViewById(R.id.signInButton);
        signInButton.performClick();

        molly.verifyNavigatedTo(launchActivity, SignInPinCaptureActivity.class);
    }

    @Test
    public void givenSessionHasNoPin_whenClickingSignIn_expectNavigationToSignInPhoneCapture() {
        UserSessionManager sessionManager = new UserSessionManager(launchActivity);
        sessionManager.createUserPin(null);

        Button signInButton = launchActivity.findViewById(R.id.signInButton);
        signInButton.performClick();

        molly.verifyNavigatedTo(launchActivity, SignInPhoneCaptureActivity.class);
    }

    @Test
    public void whenClickingSignUp_expectNavigationToRegistration() {
        Button signUpButton = launchActivity.findViewById(R.id.signUpButton);
        signUpButton.performClick();

        molly.verifyNavigatedTo(launchActivity, RegistrationNameCaptureActivity.class);
    }
}
