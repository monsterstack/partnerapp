package monsterstack.io.partner;

import android.app.Activity;
import android.view.View;

import javax.inject.Inject;

import butterknife.ButterKnife;
import monsterstack.io.partner.common.Control;
import monsterstack.io.partner.control.LaunchControl;
import monsterstack.io.partner.control.MainControl;
import monsterstack.io.partner.main.control.GroupChatControl;
import monsterstack.io.partner.main.control.GroupCreationControl;
import monsterstack.io.partner.main.control.GroupFragmentControl;
import monsterstack.io.partner.main.control.GroupScheduleControl;
import monsterstack.io.partner.main.control.GroupTransactionsControl;
import monsterstack.io.partner.main.control.InviteMembersControl;
import monsterstack.io.partner.main.control.MemberControl;
import monsterstack.io.partner.main.control.WalletsFragmentControl;
import monsterstack.io.partner.main.presenter.GroupChatPresenter;
import monsterstack.io.partner.main.presenter.GroupCreationPresenter;
import monsterstack.io.partner.main.presenter.GroupFragmentPresenter;
import monsterstack.io.partner.main.presenter.GroupSchedulePresenter;
import monsterstack.io.partner.main.presenter.GroupTransactionsPresenter;
import monsterstack.io.partner.main.presenter.InviteMembersPresenter;
import monsterstack.io.partner.main.presenter.MemberPresenter;
import monsterstack.io.partner.main.presenter.WalletFragmentPresenter;
import monsterstack.io.partner.main.presenter.impl.GroupChatPresenterImpl;
import monsterstack.io.partner.main.presenter.impl.GroupCreationPresenterImpl;
import monsterstack.io.partner.main.presenter.impl.GroupFragmentPresenterImpl;
import monsterstack.io.partner.main.presenter.impl.GroupSchedulePresenterImpl;
import monsterstack.io.partner.main.presenter.impl.GroupTransactionsPresenterImpl;
import monsterstack.io.partner.main.presenter.impl.InviteMembersPresenterImpl;
import monsterstack.io.partner.main.presenter.impl.MemberPresenterImpl;
import monsterstack.io.partner.main.presenter.impl.WalletFragmentPresenterImpl;
import monsterstack.io.partner.menu.control.BackupControl;
import monsterstack.io.partner.menu.control.BuyCurrencyControl;
import monsterstack.io.partner.menu.control.ProfileControl;
import monsterstack.io.partner.menu.control.WalletsControl;
import monsterstack.io.partner.menu.presenter.BackupPresenter;
import monsterstack.io.partner.menu.presenter.BuyCurrencyPresenter;
import monsterstack.io.partner.menu.presenter.ProfilePresenter;
import monsterstack.io.partner.menu.presenter.WalletsPresenter;
import monsterstack.io.partner.presenter.LaunchPresenter;
import monsterstack.io.partner.presenter.MainPresenter;
import monsterstack.io.partner.presenter.impl.LaunchPresenterImpl;
import monsterstack.io.partner.presenter.impl.MainPresenterImpl;
import monsterstack.io.partner.registration.control.RegistrationEmailCaptureControl;
import monsterstack.io.partner.registration.control.RegistrationNameCaptureControl;
import monsterstack.io.partner.registration.presenter.RegistrationEmailCapturePresenter;
import monsterstack.io.partner.registration.presenter.RegistrationNameCapturePresenter;
import monsterstack.io.partner.registration.presenter.impl.RegistrationEmailCapturePresenterImpl;
import monsterstack.io.partner.registration.presenter.impl.RegistrationNameCapturePresenterImpl;
import monsterstack.io.partner.settings.control.TwoStepVerificationSettingsControl;
import monsterstack.io.partner.settings.presenter.TwoStepVerificationSettingsPresenter;

public class PresenterFactory {
    @Inject
    public PresenterFactory() {}

    public MainPresenter getMainPresenter(Control control, Activity activity) {
        MainPresenterImpl presenter = new MainPresenterImpl();
        presenter.bind((MainControl)control);

        ButterKnife.bind(presenter, activity);

        return presenter;
    }

    public LaunchPresenter getLaunchPresenter(Control control, Activity activity) {
        LaunchPresenterImpl presenter = new LaunchPresenterImpl();
        presenter.bind((LaunchControl) control);

        ButterKnife.bind(presenter, activity);

        return presenter;
    }

    public GroupFragmentPresenter getGroupsFragmentPresenter(Control control, View view) {
        GroupFragmentPresenterImpl presenter = new GroupFragmentPresenterImpl();
        presenter.bind((GroupFragmentControl)control);

        ButterKnife.bind(presenter, view);

        return presenter;
    }

    public WalletFragmentPresenter getWalletFragmentPresenter(Control control, View view) {
        WalletFragmentPresenter presenter = new WalletFragmentPresenterImpl();
        presenter.bind((WalletsFragmentControl)control);

        ButterKnife.bind(presenter, view);

        return presenter;
    }

    public GroupCreationPresenter getGroupCreationPresenter(Control control, Activity activity) {
        GroupCreationPresenterImpl presenter = new GroupCreationPresenterImpl();
        presenter.bind((GroupCreationControl)control);
        ButterKnife.bind(presenter, activity);
        return presenter;
    }

    public GroupSchedulePresenter getGroupSchedulePresenter(Control control, Activity activity) {
        GroupSchedulePresenterImpl presenter = new GroupSchedulePresenterImpl();
        presenter.bind((GroupScheduleControl)control);
        ButterKnife.bind(presenter, activity);
        return presenter;
    }

    public GroupTransactionsPresenter getGroupTransactionsPresenter(Control control, Activity activity) {
        GroupTransactionsPresenterImpl presenter = new GroupTransactionsPresenterImpl();
        presenter.bind((GroupTransactionsControl)control);

        ButterKnife.bind(presenter, activity);
        return presenter;
    }

    public GroupChatPresenter getGroupChatPresenter(Control control, Activity activity) {
        GroupChatPresenterImpl presenter = new GroupChatPresenterImpl();
        presenter.bind((GroupChatControl)control);

        ButterKnife.bind(presenter, activity);

        return presenter;
    }

    public InviteMembersPresenter getInviteMembersPresenter(Control control, Activity activity) {
        InviteMembersPresenterImpl presenter = new InviteMembersPresenterImpl(control);
        presenter.bind((InviteMembersControl)control);

        ButterKnife.bind(presenter, activity);
        return presenter;
    }

    public MemberPresenter getMemberPresenter(Control control, Activity activity) {
        MemberPresenter presenter = new MemberPresenterImpl();
        presenter.bind((MemberControl)control);

        ButterKnife.bind(presenter, activity);
        return presenter;
    }

    public ProfilePresenter getProfilePresenter(Control control, Activity activity) {
        ProfilePresenter presenter = new ProfilePresenter();
        presenter.bind((ProfileControl)control);

        ButterKnife.bind(presenter, activity);
        return presenter;
    }

    public TwoStepVerificationSettingsPresenter getTwoSetVerificationSettingsPresenter(Control control, Activity activity) {
        TwoStepVerificationSettingsPresenter presenter = new TwoStepVerificationSettingsPresenter();
        presenter.bind((TwoStepVerificationSettingsControl)control);

        ButterKnife.bind(presenter, activity);
        return presenter;
    }

    public WalletsPresenter getWalletsPresenter(Control control, Activity activity) {
        WalletsPresenter presenter = new WalletsPresenter();
        presenter.bind((WalletsControl) control);

        ButterKnife.bind(presenter, activity);
        return presenter;
    }

    public BuyCurrencyPresenter getBuyCurrencyPresenter(Control control, Activity activity) {
        BuyCurrencyPresenter presenter = new BuyCurrencyPresenter();
        ButterKnife.bind(presenter, activity);
        presenter.bind((BuyCurrencyControl)control);
        return presenter;
    }

    public RegistrationNameCapturePresenter getRegistrationNameCapturePresenter(Control control, Activity activity) {
        RegistrationNameCapturePresenter presenter = new RegistrationNameCapturePresenterImpl(control.getContext());

        presenter.bind((RegistrationNameCaptureControl)control);

        ButterKnife.bind(presenter, activity);

        return presenter;
    }

    public RegistrationEmailCapturePresenter getRegistrationEmailCapturePresenter(Control control, Activity activity) {
        RegistrationEmailCapturePresenter presenter = new RegistrationEmailCapturePresenterImpl();

        ButterKnife.bind(presenter, activity);
        presenter.bind((RegistrationEmailCaptureControl)control);

        return presenter;
    }

    public BackupPresenter getBackupPresenter(Control control, Activity activity) {
        BackupPresenter presenter = new BackupPresenter();
        presenter.bind((BackupControl)control);

        ButterKnife.bind(presenter, activity);

        return presenter;
    }
}
