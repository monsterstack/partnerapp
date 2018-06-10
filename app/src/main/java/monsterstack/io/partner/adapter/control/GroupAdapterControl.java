package monsterstack.io.partner.adapter.control;

import monsterstack.io.api.listeners.OnResponseListener;
import monsterstack.io.api.resources.HttpError;
import monsterstack.io.partner.adapter.ViewAnimatedListener;
import monsterstack.io.partner.common.Control;
import monsterstack.io.partner.domain.Group;
import monsterstack.io.partner.domain.Member;

public interface GroupAdapterControl extends Control {
    void findGroupMembers(Group group, final OnResponseListener<Member[], HttpError> onResponseListener);
    ViewAnimatedListener getViewAnimatedListener();

    void onMemberSelected(Member selectedMember);
}
