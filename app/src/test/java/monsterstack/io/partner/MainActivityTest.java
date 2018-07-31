package monsterstack.io.partner;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;
import org.powermock.reflect.Whitebox;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;

import monsterstack.io.api.resources.AuthenticatedUser;
import monsterstack.io.avatarview.AvatarView;
import monsterstack.io.partner.common.presenter.Presenter;
import monsterstack.io.partner.control.MainControl;
import monsterstack.io.partner.data.Personas;
import monsterstack.io.partner.menu.BackupActivity;
import monsterstack.io.partner.menu.BuyCurrencyActivity;
import monsterstack.io.partner.menu.ProfileActivity;
import monsterstack.io.partner.menu.SettingsActivity;
import monsterstack.io.partner.presenter.MainPresenter;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricTestRunner.class)
@Config(
        sdk = 27,
        constants = BuildConfig.class)
public class MainActivityTest {

    private MainActivity mainActivity;

    private Personas.Molly molly;

    @Captor
    private ArgumentCaptor<AuthenticatedUser> authenticatedUserArgumentCaptor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        ActivityController<MainActivity> controller = Robolectric.buildActivity(MainActivity.class);
        // Authenticated as Molly
        molly = new Personas().asMolly().authenticate(controller.get());

        mainActivity = controller.setup().get();
    }

    @Test
    public void onProfileClick_expectProfileNavHeaderMatchesPersona() {
        assertNotNull(mainActivity.presenterFactory);
        Presenter<MainControl> presenter = Whitebox.getInternalState(mainActivity, "presenter");
        assertNotNull(presenter);

        // Ids should be self describing..
        TextView profileFullName = mainActivity.findViewById(R.id.profile_fullname);
        TextView profileEmail = mainActivity.findViewById(R.id.profile_email);
        AvatarView profileAvatar = mainActivity.findViewById(R.id.userImage);

        assertEquals(molly.getEmailAddress(), profileEmail.getText().toString());
        assertEquals(molly.getFullName(), profileFullName.getText().toString());
        assertEquals(molly.getAvatarUrl(), profileAvatar.getUser().getAvatarUrl());
    }

    @Test
    public void onProfileClick_expectNavigationToProfile() {
        NavigationView navigationView = mainActivity.findViewById(R.id.nav_view);
        View navHeader = navigationView.getHeaderView(0);
        // Trigger Navigation
        navHeader.performClick();

        molly.verifyNavigatedTo(mainActivity, ProfileActivity.class);
    }

    @Test
    public void onBuyCurrencyClick_expectNavigationToBuyCurrency() {
        NavigationView navigationView = mainActivity.findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();
        menu.performIdentifierAction(R.id.nav_buy, 0);

        molly.verifyNavigatedTo(mainActivity, BuyCurrencyActivity.class);
    }

    @Test
    public void onBackupClick_expectNavigationToBackup() {
        NavigationView navigationView = mainActivity.findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();
        menu.performIdentifierAction(R.id.nav_backup, 0);

        molly.verifyNavigatedTo(mainActivity, BackupActivity.class);
    }

    @Test
    public void onSettingsClick_expectNavigationToSettings() {
        NavigationView navigationView = mainActivity.findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();

        menu.performIdentifierAction(R.id.nav_settings, 0);

        molly.verifyNavigatedTo(mainActivity, SettingsActivity.class);
    }

    @Test
    public void onLogoutClick_expectNavigationToLaunch() {
        NavigationView navigationView = mainActivity.findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();

        menu.performIdentifierAction(R.id.nav_logout, 0);

        molly.verifyNavigatedTo(mainActivity, LaunchActivity.class);
    }

    @Test
    public void onActivityResultFromProfileActivity_expectAvatarUpdate() {
        // Swap out presenter with a spy
        MainPresenter spiedOnPresenter = spy((MainPresenter)
                Whitebox.getInternalState(mainActivity, "presenter"));
        Whitebox.setInternalState(mainActivity, "presenter", spiedOnPresenter);

        mainActivity.goToProfile();

        shadowOf(mainActivity).receiveResult(
                new Intent(mainActivity, ProfileActivity.class),
                MainActivity.PROFILE_UPDATE_REQUEST_CODE,
                new Intent());

        verify(spiedOnPresenter, times(1))
                .updateAvatarWithAuthenticatedUser(authenticatedUserArgumentCaptor.capture());

        AuthenticatedUser capturedAuthenticatedUser = authenticatedUserArgumentCaptor.getValue();

        assertEquals(molly.getAvatarUrl(), capturedAuthenticatedUser.getAvatarUrl());
        assertEquals(molly.getFullName(), capturedAuthenticatedUser.getFullName());
    }
}
