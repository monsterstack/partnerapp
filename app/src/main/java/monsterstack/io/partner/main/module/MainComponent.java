package monsterstack.io.partner.main.module;

import javax.inject.Singleton;

import dagger.Component;
import monsterstack.io.partner.common.BasicActivity;
import monsterstack.io.partner.main.GroupChatActivity;
import monsterstack.io.partner.main.GroupCreationActivity;
import monsterstack.io.partner.main.GroupScheduleActivity;
import monsterstack.io.partner.main.GroupTransactionsActivity;
import monsterstack.io.partner.main.InviteMembersActivity;
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

    void inject(MessagingService messagingService);
    void inject(AnalyticsService analyticsService);

    // Menu
    void inject(MenuActivity menuActivity);
    void inject(ProfileActivity profileActivity);
}
