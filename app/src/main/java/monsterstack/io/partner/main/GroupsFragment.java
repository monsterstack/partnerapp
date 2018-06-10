package monsterstack.io.partner.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.appinvite.AppInviteInvitation;
import com.leinardi.android.speeddial.SpeedDialActionItem;
import com.leinardi.android.speeddial.SpeedDialView;

import java.util.Map;
import java.util.Optional;

import butterknife.ButterKnife;
import monsterstack.io.api.listeners.OnResponseListener;
import monsterstack.io.api.resources.HttpError;
import monsterstack.io.partner.R;
import monsterstack.io.partner.adapter.GroupAdapter;
import monsterstack.io.partner.adapter.ViewAnimatedListener;
import monsterstack.io.partner.domain.Group;
import monsterstack.io.partner.main.control.GroupFragmentControl;
import monsterstack.io.partner.main.presenter.GroupFragmentPresenter;
import monsterstack.io.partner.utils.NavigationUtils;

public class GroupsFragment extends Fragment implements GroupFragmentControl {
    private View view;

    private Group selectedGroup;

    private GroupFragmentPresenter presenter;

    public static GroupsFragment newInstance(String title) {
        Bundle args = new Bundle();
        args.putString("title", title);
        GroupsFragment instance = new GroupsFragment();

        instance.setArguments(args);

        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        this.view = inflater.inflate(
                R.layout.fragment_groups, container, false);

        presenter = new GroupFragmentPresenter(this);
        ButterKnife.bind(presenter, this.view);

        presenter.present(Optional.<Map>empty());

        return this.view;
    }

    @Override
    public void viewGroupSchedule(Group group) {
        Log.d("ViewSched", "view schedule");
        Bundle bundle = NavigationUtils.enterStageRightBundle(getContext());
        Intent intent = new Intent(getContext(), GroupScheduleActivity.class);
        intent.putExtra(GroupScheduleActivity.EXTRA_GROUP, group);
        startActivity(intent, bundle);
    }

    @Override
    public void viewGroupChat(Group group) {
        Log.d("ViewChat", "view chat");
        Bundle bundle = NavigationUtils.enterStageRightBundle(getContext());
        Intent intent = new Intent(getContext(), GroupChatActivity.class);
        intent.putExtra(GroupChatActivity.EXTRA_GROUP, group);
        startActivity(intent, bundle);
    }

    @Override
    public void inviteMembers(Group group) {
        Intent intent = new AppInviteInvitation.IntentBuilder("Welcome to Partner")
                .setMessage("You've been invited to join this Partner")
                .setDeepLink(Uri.parse("http://localhost:2020/invite.html"))
                .setCallToActionText("Join Now")
                .build();
        startActivityForResult(intent, 1000);

        if (null != getActivity())
            getActivity().overridePendingTransition(R.anim.slide_left, R.anim.slide_right);
    }

    @Override
    public void createGroup() {
        Bundle bundle = NavigationUtils.enterStageRightBundle(getContext());
        Intent intent = new Intent(getContext(), GroupCreationActivity.class);
        startActivity(intent, bundle);
    }

    @Override
    public void viewGroupTransactions(Group group) {
        Bundle bundle = NavigationUtils.enterStageRightBundle(getContext());
        Intent intent = new Intent(getContext(), GroupTransactionsActivity.class);
        intent.putExtra(GroupTransactionsActivity.EXTRA_GROUP, group);
        startActivity(intent, bundle);
    }

    @Override
    public void inflateFloatingActionButton(Group[] groups) {
        presenter.inflateSpeedDial();
    }

    @Override
    public void listenForCardViewAnimationEvents(GroupAdapter cardAdapter) {
        cardAdapter.setViewAnimatedListener(new ViewAnimatedListener() {
            @Override
            public void onViewExpanded() {
                presenter.hideSpeedDialView();
                presenter.pauseViewPager();
            }

            @Override
            public void onViewCollapsed() {
                presenter.showSpeedDialView();
                presenter.unpauseViewPager();
            }
        });
    }

    @Override
    public void listenForSpeedDialSelections(SpeedDialView speedDial) {
        speedDial.setOnActionSelectedListener(new SpeedDialView.OnActionSelectedListener() {
            @Override
            public boolean onActionSelected(SpeedDialActionItem speedDialActionItem) {
                Group group = GroupsFragment.this.selectedGroup;

                switch (speedDialActionItem.getId()) {
                    case R.id.view_schedule_action:
                        Log.d("SpeedDial","Link action clicked!");
                        viewGroupSchedule(group);
                        return false; // true to keep the Speed Dial open
                    case R.id.group_chat_action:
                        viewGroupChat(group);
                        return false;
                    case R.id.invite_members_action:
                        inviteMembers(group);
                        return false;
                    case R.id.add_group_action:
                        createGroup();
                        return false;
                    case R.id.view_transactions_action:
                        viewGroupTransactions(group);
                        return false;
                    default:
                        return false;
                }
            }
        });
    }

    @Override
    public void setSelectedGroup(Group group) {
        this.selectedGroup = group;
    }

    @Override
    public void findGroupsAssociatedWithUser(OnResponseListener<Group[], HttpError> onResponseListener) {
        Group[] groups = {
                new Group("Family", 10),
                new Group("Friends", 5),
                new Group("Coworkers", 4),
                new Group("Acquaintances", 34),
                new Group("Enemies", 23),
                new Group("Frenemies", 21)
        };
        onResponseListener.onResponse(groups, null);
    }
}
