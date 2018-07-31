package monsterstack.io.partner.main.presenter;

import android.view.View;

import monsterstack.io.partner.common.presenter.Presenter;
import monsterstack.io.partner.domain.Contact;
import monsterstack.io.partner.main.control.InviteMembersControl;

public interface InviteMembersPresenter extends Presenter<InviteMembersControl> {
    Integer MAX_SELECTIONS_ALLOWED = 12;

    void addSelectedContact(Contact contact);

    void removeSelectedContact(Contact contact);

    void toggleBottomSheet();

    void onMemberInviteBottomSheetSlide(float slideOffset);

    void onMemberInviteBottomSheetStateChanged(int newState);

    void onMemberInviteBottomSheetHeaderClicked();

    View getRootView();
}
