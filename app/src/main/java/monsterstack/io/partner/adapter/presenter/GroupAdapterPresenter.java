package monsterstack.io.partner.adapter.presenter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import butterknife.BindView;
import monsterstack.io.avatarview.AvatarView;
import monsterstack.io.gridlistview.GridListView;
import monsterstack.io.gridlistview.SpacesItemDecoration;
import monsterstack.io.partner.R;
import monsterstack.io.partner.adapter.MemberRecyclerViewAdapter;
import monsterstack.io.partner.adapter.control.GroupAdapterControl;
import monsterstack.io.partner.anim.AnimationOptions;
import monsterstack.io.partner.anim.GroupCardJoinAnimator;
import monsterstack.io.partner.anim.GroupCardMemberAnimator;
import monsterstack.io.partner.common.presenter.Presenter;
import monsterstack.io.partner.domain.Group;
import monsterstack.io.partner.domain.GroupEntryOpportunity;
import monsterstack.io.partner.domain.Member;

import static android.view.View.VISIBLE;

public class GroupAdapterPresenter implements Presenter<GroupAdapterControl> {
    @BindView(R.id.cardView)
    CardView cardView;
    @BindView(R.id.membersView)
    RecyclerView memberDetailsView;

    @BindView(R.id.memberAvatarView)
    AvatarView selectedMemberAvatar;

    @BindView(R.id.group_details_name)
    TextView groupNameView;

    @BindView(R.id.group_details_groupBalance)
    TextView groupBalanceView;

    private Member selectedMember;

    private GroupAdapterControl control;

    @Override
    public Presenter<GroupAdapterControl> present(Optional<Map> metadata) {
        if (metadata.isPresent() && metadata.get().containsKey("group") ) {
            final Group data = (Group) metadata.get().get("group");

            // Set the Group name
            String name = data.getName();
            groupNameView.setText(name);

            Double drawAmount = data.getDrawAmount();
            NumberFormat format = NumberFormat.getCurrencyInstance(Locale.US);
            String currency = format.format(drawAmount);
            groupBalanceView.setText(currency);

            final GridListView recyclerView = cardView.findViewById(R.id.membersView);
            recyclerView.addItemDecoration(new SpacesItemDecoration(control.getContext(), R.dimen.member_grid_item_offset));
            control.findGroupMembers(data, (members, httpError) -> {
                final MemberRecyclerViewAdapter adapter = new MemberRecyclerViewAdapter(cardView.getContext(), members);
                adapter.setClickListener((view, position) -> {
                    if (recyclerView.getVisibility() == VISIBLE) {
                        if (null != members[position]) {
                            Member clickedMember = members[position];
                            GroupAdapterPresenter.this.selectedMember = clickedMember;
                            new GroupCardMemberAnimator(cardView,
                                    AnimationOptions.options(350, 300)
                                            .onViewAnimatedListener(control.getViewAnimatedListener())
                            ).animateIn(clickedMember);
                        } else {
                            new GroupCardJoinAnimator(cardView,
                                    AnimationOptions.options(350, 300)
                                            .onViewAnimatedListener(control.getViewAnimatedListener())
                            ).animateIn(new GroupEntryOpportunity(data, position + 1));
                        }
                    }
                });

                recyclerView.setAdapter(adapter);
            });

            selectedMemberAvatar.setOnClickListener(v -> control.onMemberSelected(GroupAdapterPresenter.this.selectedMember));
        }

        return this;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public Presenter<GroupAdapterControl> bind(GroupAdapterControl control) {
        this.control = control;

        return this;
    }

    @Override
    public GroupAdapterControl getControl() {
        return control;
    }
}
