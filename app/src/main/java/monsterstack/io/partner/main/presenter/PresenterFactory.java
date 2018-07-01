package monsterstack.io.partner.main.presenter;

import android.app.Activity;

import javax.inject.Inject;

import butterknife.ButterKnife;
import monsterstack.io.partner.common.Control;
import monsterstack.io.partner.main.control.GroupCreationControl;
import monsterstack.io.partner.main.control.GroupScheduleControl;
import monsterstack.io.partner.main.control.GroupTransactionsControl;
import monsterstack.io.partner.main.control.InviteMembersControl;
import monsterstack.io.partner.main.control.MemberControl;
import monsterstack.io.partner.menu.control.BackupControl;
import monsterstack.io.partner.menu.control.BuyCurrencyControl;
import monsterstack.io.partner.menu.control.ProfileControl;
import monsterstack.io.partner.menu.control.WalletsControl;
import monsterstack.io.partner.menu.presenter.BackupPresenter;
import monsterstack.io.partner.menu.presenter.BuyCurrencyPresenter;
import monsterstack.io.partner.menu.presenter.ProfilePresenter;
import monsterstack.io.partner.menu.presenter.WalletsPresenter;
import monsterstack.io.partner.settings.control.TwoStepVerificationSettingsControl;
import monsterstack.io.partner.settings.presenter.TwoStepVerificationSettingsPresenter;

public class PresenterFactory {
    @Inject
    public PresenterFactory() {}

    public GroupCreationPresenter getGroupCreationPresenter(Control control, Activity activity) {
        GroupCreationPresenter presenter = new GroupCreationPresenter();
        presenter.bind((GroupCreationControl)control);
        ButterKnife.bind(presenter, activity);
        return presenter;
    }

    public GroupSchedulePresenter getGroupSchedulePresenter(Control control, Activity activity) {
        GroupSchedulePresenter presenter = new GroupSchedulePresenter();
        presenter.bind((GroupScheduleControl)control);
        ButterKnife.bind(presenter, activity);
        return presenter;
    }

    public GroupTransactionsPresenter getGroupTransactionsPresenter(Control control, Activity activity) {
        GroupTransactionsPresenter presenter = new GroupTransactionsPresenter();
        presenter.bind((GroupTransactionsControl)control);

        ButterKnife.bind(presenter, activity);
        return presenter;
    }

    public InviteMembersPresenter getInviteMembersPresenter(Control control, Activity activity) {
        InviteMembersPresenter presenter = new InviteMembersPresenter(control);
        presenter.bind((InviteMembersControl)control);

        ButterKnife.bind(presenter, activity);
        return presenter;
    }

    public MemberPresenter getMemberPresenter(Control control, Activity activity) {
        MemberPresenter presenter = new MemberPresenter();
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
        presenter.bind((BuyCurrencyControl)control);

        ButterKnife.bind(presenter, activity);
        return presenter;
    }

    public BackupPresenter getBackupPresenter(Control control, Activity activity) {
        BackupPresenter presenter = new BackupPresenter();
        presenter.bind((BackupControl)control);

        ButterKnife.bind(presenter, activity);

        return presenter;
    }
}
