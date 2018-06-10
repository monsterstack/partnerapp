package monsterstack.io.partner.adapter.presenter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.Map;
import java.util.Optional;

import butterknife.BindView;
import monsterstack.io.api.listeners.OnResponseListener;
import monsterstack.io.api.resources.HttpError;
import monsterstack.io.avatarview.AvatarView;
import monsterstack.io.gridlistview.GridListView;
import monsterstack.io.gridlistview.SpacesItemDecoration;
import monsterstack.io.partner.R;
import monsterstack.io.partner.adapter.MemberRecyclerViewAdapter;
import monsterstack.io.partner.adapter.control.GroupAdapterControl;
import monsterstack.io.partner.anim.AnimationOptions;
import monsterstack.io.partner.anim.GroupCardJoinAnimator;
import monsterstack.io.partner.anim.GroupCardMemberAnimator;
import monsterstack.io.partner.common.presenter.PresenterAdapter;
import monsterstack.io.partner.domain.Group;
import monsterstack.io.partner.domain.GroupEntryOpportunity;
import monsterstack.io.partner.domain.Member;

import static android.view.View.VISIBLE;

public class GroupAdapterPresenter extends PresenterAdapter {
    @BindView(R.id.cardView)
    CardView cardView;
    @BindView(R.id.membersView)
    RecyclerView memberDetailsView;

    @BindView(R.id.memberAvatarView)
    AvatarView selectedMemberAvatar;

    private Member selectedMember;

    private GroupAdapterControl control;

    public GroupAdapterPresenter(GroupAdapterControl control) {
        this.control = control;
    }

    @Override
    public void present(Optional<Map> metadata) {
        if (metadata.isPresent() && metadata.get().containsKey("group") ) {
            final Group data = (Group) metadata.get().get("group");
            // @TODO: All falls apart when this is moved into method.
            final GridListView recyclerView = cardView.findViewById(R.id.membersView);
            recyclerView.addItemDecoration(new SpacesItemDecoration(control.getContext(), R.dimen.member_grid_item_offset));
            control.findGroupMembers(data, new OnResponseListener<Member[], HttpError>() {
                @Override
                public void onResponse(final Member[] members, HttpError httpError) {
                    final MemberRecyclerViewAdapter adapter = new MemberRecyclerViewAdapter(cardView.getContext(), members);
                    adapter.setClickListener(new MemberRecyclerViewAdapter.ItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
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
                        }
                    });

                    recyclerView.setAdapter(adapter);
                }
            });

            selectedMemberAvatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    control.onMemberSelected(GroupAdapterPresenter.this.selectedMember);
                }
            });
        }
    }
}
