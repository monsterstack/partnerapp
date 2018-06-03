package monsterstack.io.partner.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.appinvite.AppInviteInvitation;
import com.leinardi.android.speeddial.SpeedDialActionItem;
import com.leinardi.android.speeddial.SpeedDialView;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import monsterstack.io.api.listeners.OnResponseListener;
import monsterstack.io.api.resources.HttpError;
import monsterstack.io.partner.R;
import monsterstack.io.partner.adapter.GroupAdapter;
import monsterstack.io.partner.adapter.MemberRecyclerViewAdapter;
import monsterstack.io.partner.adapter.ViewAnimatedListener;
import monsterstack.io.partner.domain.Group;
import monsterstack.io.partner.utils.NavigationUtils;
import monsterstack.io.streamview.PausingViewPager;
import monsterstack.io.streamview.ShadowTransformer;

import static android.support.v4.view.ViewPager.SCROLL_STATE_DRAGGING;
import static android.support.v4.view.ViewPager.SCROLL_STATE_SETTLING;

public class GroupsFragment extends Fragment {
    private View view;

    private MemberRecyclerViewAdapter adapter;
    private Group selectedGroup;

    @BindView(R.id.viewPager)
    PausingViewPager viewPager;

    @BindView(R.id.speedDial)
    SpeedDialView speedDialView;

    // Array of strings...
    private Group[] groupArray;

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

        ButterKnife.bind(this, this.view);

        initialize();

        return this.view;
    }

    public void viewGroupSchedule(Group group) {
        Log.d("ViewSched", "view schedule");
        Bundle bundle = NavigationUtils.enterStageRightBundle(getContext());
        Intent intent = new Intent(getContext(), GroupScheduleActivity.class);
        intent.putExtra(GroupScheduleActivity.EXTRA_GROUP, group);
        startActivity(intent, bundle);
    }

    public void viewGroupChat(Group group) {
        Log.d("ViewChat", "view chat");
        Bundle bundle = NavigationUtils.enterStageRightBundle(getContext());
        Intent intent = new Intent(getContext(), GroupChatActivity.class);
        intent.putExtra(GroupChatActivity.EXTRA_GROUP, group);
        startActivity(intent, bundle);
    }

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

    public void createGroup() {
        Bundle bundle = NavigationUtils.enterStageRightBundle(getContext());
        Intent intent = new Intent(getContext(), GroupCreationActivity.class);
        startActivity(intent, bundle);
    }

    public void viewGroupTransactions(Group group) {
        Bundle bundle = NavigationUtils.enterStageRightBundle(getContext());
        Intent intent = new Intent(getContext(), GroupTransactionsActivity.class);
        intent.putExtra(GroupTransactionsActivity.EXTRA_GROUP, group);
        startActivity(intent, bundle);
    }

    private void inflateFloatingActionButton(Group[] groups) {
        if (groups.length == 0) {
            speedDialView.inflate(R.menu.group_view_speed_dial_minimal);
        } else {
            speedDialView.inflate(R.menu.group_view_speed_dial);
        }
    }

    private void listenForCardViewAnimationEvents(GroupAdapter cardAdapter) {
        cardAdapter.setViewAnimatedListener(new ViewAnimatedListener() {
            @Override
            public void onViewExpanded() {
                speedDialView.hide();
                viewPager.setPaused(true);
            }

            @Override
            public void onViewCollapsed() {
                speedDialView.show();
                viewPager.setPaused(false);
            }
        });
    }

    private void prepareViewPager(final ViewPager viewPager, final GroupAdapter cardAdapter) {
        int margin = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, -20,
                getResources().getDisplayMetrics());
        viewPager.setPageMargin(-margin);

        viewPager.setPageMargin(margin);
        viewPager.setAdapter(cardAdapter);
        viewPager.setPageTransformer(false, new ShadowTransformer(viewPager, cardAdapter));
        viewPager.setOffscreenPageLimit(5);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                GroupsFragment.this.selectedGroup = groupArray[position];
                cardAdapter.reset();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == SCROLL_STATE_DRAGGING)
                    speedDialView.hide();
                else if (state == SCROLL_STATE_SETTLING)
                    speedDialView.show();
                else
                    speedDialView.show();
            }
        });
    }

    private void listenForSpeedDialSelections(SpeedDialView speedDial) {
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

    private void findGroupsAssociatedWithUser(OnResponseListener<Group[], HttpError> onResponseListener) {
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

    private void initialize() {
        findGroupsAssociatedWithUser(new OnResponseListener<Group[], HttpError>() {
            @Override
            public void onResponse(Group[] groups, HttpError httpError) {
                groupArray = groups;
                final GroupAdapter cardAdapter = new GroupAdapter(getContext(), Arrays.asList(groupArray));

                inflateFloatingActionButton(groups);

                listenForCardViewAnimationEvents(cardAdapter);

                prepareViewPager(viewPager, cardAdapter);

                listenForSpeedDialSelections(speedDialView);

            }
        });
    }
}
