package monsterstack.io.partner.anim;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import monsterstack.io.partner.R;
import monsterstack.io.partner.domain.GroupEntryOpportunity;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class GroupCardJoinAnimator extends ViewAnimator<CardView, GroupEntryOpportunity> {

    @BindView(R.id.group_slot_label)
    TextView slotLabel;

    @BindView(R.id.miniGroupJoin)
    RelativeLayout groupJoinView;

    @BindView(R.id.capacity)
    View capacityView;

    @BindView(R.id.membersView)
    RecyclerView membersView;

    private AnimationOptions animationOptions;

    public GroupCardJoinAnimator(CardView root, AnimationOptions animationOptions) {
        super(animationOptions);
        ButterKnife.bind(this, root);
    }

    @Override
    public void animateIn(GroupEntryOpportunity data) {
        hideMemberList();
        showGroupJoin(data);
    }

    @Override
    public void animateOut() {
        showMemberList();
        hideGroupJoin();
    }

    private void showGroupJoin(final GroupEntryOpportunity groupEntryOpportunity) {
        bindGroupToGroupJoinView(groupEntryOpportunity);

        groupJoinView.setVisibility(VISIBLE);
        slotLabel.setVisibility(VISIBLE);

        Animation animation = new TranslateAnimation(1200, 0, 0, 0);
        animation.setInterpolator(getAnimationOptions().getInterpolator());

        animation.setDuration(getAnimationOptions().getExpandDuration());
        animation.setFillAfter(true);

        // When done animating, apply click listener to incoming view
        animation.setAnimationListener(new AnimationListenerAdapter() {

            @Override
            public void onAnimationEnd(Animation animation) {
                //View slotLabel = card.findViewById(R.id.group_slot_label);
                slotLabel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        animateOut();
                    }
                });

                getAnimationOptions().getViewAnimatedListener().onViewExpanded();
            }
        });

        groupJoinView.startAnimation(animation);
    }

    private void showMemberList() {
        this.membersView.setVisibility(VISIBLE);
        this.capacityView.setVisibility(VISIBLE);
        Animation animation = new TranslateAnimation(-1200, 0,0, 0); //May need to check the direction you want.
        animation.setInterpolator(getAnimationOptions().getInterpolator());

        animation.setDuration(getAnimationOptions().getExpandDuration());
        animation.setFillAfter(true);

        this.membersView.startAnimation(animation);
        this.capacityView.startAnimation(animation);
    }

    private void hideGroupJoin() {
        Animation animation = new TranslateAnimation(0, 1200,0, 0); //May need to check the direction you want.
        animation.setInterpolator(getAnimationOptions().getInterpolator());

        animation.setDuration(getAnimationOptions().getCollapseDuration());
        animation.setFillAfter(true);

        animation.setAnimationListener(new AnimationListenerAdapter() {

            @Override
            public void onAnimationEnd(Animation animation) {
                slotLabel.setVisibility(View.INVISIBLE);
                getAnimationOptions().getViewAnimatedListener().onViewCollapsed();
            }
        });

        this.groupJoinView.startAnimation(animation);
    }

    /**
     * Hide Member List from Card
     */
    private void hideMemberList() {
        Animation animation = new TranslateAnimation(0, -1200,0, 0); //May need to check the direction you want.
        animation.setInterpolator(getAnimationOptions().getInterpolator());

        animation.setDuration(getAnimationOptions().getCollapseDuration());
        animation.setFillAfter(true);

        this.membersView.startAnimation(animation);
        this.capacityView.startAnimation(animation);
        this.capacityView.setVisibility(GONE);
        this.membersView.setVisibility(GONE);
    }

    private void bindGroupToGroupJoinView(GroupEntryOpportunity groupEntryOpportunity) {
        this.slotLabel.setText("Draw Slot - " + groupEntryOpportunity.getSlotNumber());
    }
}
