package monsterstack.io.partner.main.module;

import javax.inject.Singleton;

import dagger.Component;
import monsterstack.io.partner.LaunchActivity;
import monsterstack.io.partner.MainActivity;
import monsterstack.io.partner.challenge.RegistrationPinCaptureActivity;
import monsterstack.io.partner.challenge.SignInChallengeVerificationActivity;
import monsterstack.io.partner.challenge.SignInPhoneCaptureActivity;
import monsterstack.io.partner.challenge.SignInPinCaptureActivity;
import monsterstack.io.partner.common.BasicActivity;
import monsterstack.io.partner.main.GroupChatActivity;
import monsterstack.io.partner.main.GroupCreationActivity;
import monsterstack.io.partner.main.GroupScheduleActivity;
import monsterstack.io.partner.main.GroupTransactionsActivity;
import monsterstack.io.partner.main.GroupsFragment;
import monsterstack.io.partner.main.InviteMembersActivity;
import monsterstack.io.partner.main.WalletsFragment;
import monsterstack.io.partner.menu.MenuActivity;
import monsterstack.io.partner.menu.ProfileActivity;
import monsterstack.io.partner.menu.SettingsActivity;
import monsterstack.io.partner.menu.module.MenuModule;
import monsterstack.io.partner.services.AnalyticsService;
import monsterstack.io.partner.services.MessagingService;
import monsterstack.io.partner.services.module.ServicesModule;

@Singleton
@Component(modules = {MainModule.class, ServicesModule.class, MenuModule.class})
public interface MainComponent {
    void inject(BasicActivity basicActivity);

    void inject(GroupCreationActivity groupCreationActivity);
    void inject(InviteMembersActivity inviteMembersActivity);
    void inject(GroupTransactionsActivity groupTransactionsActivity);
    void inject(GroupScheduleActivity groupScheduleActivity);
    void inject(GroupChatActivity groupChatActivity);

    void inject(SettingsActivity settingsActivity);
    void inject(RegistrationPinCaptureActivity registrationPinCaptureActivity);
    void inject(SignInChallengeVerificationActivity signInChallengeVerificationActivity);
    void inject(SignInPinCaptureActivity signInPinCaptureActivity);
    void inject(SignInPhoneCaptureActivity signInPhoneCaptureActivity);

    void inject(LaunchActivity launchActivity);
    void inject(MainActivity mainActivity);

    void inject(MessagingService messagingService);
    void inject(AnalyticsService analyticsService);

    // Menu
    void inject(MenuActivity menuActivity);
    void inject(ProfileActivity profileActivity);


    // Fragments
    void inject(GroupsFragment groupsFragment);
    void inject(WalletsFragment walletsFragment);
}
