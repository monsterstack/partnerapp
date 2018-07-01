package monsterstack.io.partner.main.control;

import com.leinardi.android.speeddial.SpeedDialView;

import monsterstack.io.api.listeners.OnResponseListener;
import monsterstack.io.api.resources.HttpError;
import monsterstack.io.partner.adapter.GroupAdapter;
import monsterstack.io.partner.common.Control;
import monsterstack.io.partner.domain.Group;

public interface GroupFragmentControl extends Control {
    void setSelectedGroup(Group group);
    void findGroupsAssociatedWithUser(boolean empty, OnResponseListener<Group[], HttpError> listener);

    void inflateFloatingActionButton(Group[] groups);

    void listenForCardViewAnimationEvents(GroupAdapter cardAdapter);

    void listenForSpeedDialSelections(SpeedDialView speedDialView);

    void viewGroupSchedule(Group group);
    void viewGroupChat(Group group);

    void inviteMembers(Group group);

    void createGroup();

    void viewGroupTransactions(Group group);
}
