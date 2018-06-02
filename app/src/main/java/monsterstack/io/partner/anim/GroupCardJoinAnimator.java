package monsterstack.io.partner.anim;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import monsterstack.io.partner.R;
import monsterstack.io.partner.domain.GroupEntryOpportunity;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class GroupCardJoinAnimator {
    public void animateIn(CardView cardView, GroupEntryOpportunity data) {
        hideMemberList(cardView);
        showGroupJoin(cardView, data);
    }

    public void animateOut(CardView cardView) {
        showMemberList(cardView);
        hideGroupJoin(cardView);
    }

    private void showGroupJoin(final View card, final GroupEntryOpportunity groupEntryOpportunity) {
        // Bind member to entering view
        final RelativeLayout groupJoinView = card.findViewById(R.id.miniGroupJoin);
        bindGroupToGroupJoinView(groupJoinView, groupEntryOpportunity);

        groupJoinView.setVisibility(VISIBLE);
        View slotLabel = card.findViewById(R.id.group_slot_label);
        slotLabel.setVisibility(VISIBLE);

        Animation animation = new TranslateAnimation(-1200, 0, 0, 0);
        animation.setDuration(1000);
        animation.setFillAfter(true);

        // When done animating, apply click listener to incoming view
        animation.setAnimationListener(new AnimationListenerAdapter() {

            @Override
            public void onAnimationEnd(Animation animation) {
                View slotLabel = card.findViewById(R.id.group_slot_label);
                slotLabel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        animateOut((CardView)card);
                    }
                });
            }
        });

        groupJoinView.startAnimation(animation);
    }

    private void showMemberList(final View card) {
        View capacityView = card.findViewById(R.id.capacity);


        RecyclerView membersView = card.findViewById(R.id.membersView);
        membersView.setVisibility(VISIBLE);
        capacityView.setVisibility(VISIBLE);
        Animation animation = new TranslateAnimation(1200, 0,0, 0); //May need to check the direction you want.
        animation.setDuration(1000);
        animation.setFillAfter(true);

        membersView.startAnimation(animation);
        capacityView.startAnimation(animation);
    }

    private void hideGroupJoin(final View card) {
        final RelativeLayout groupJoinView = card.findViewById(R.id.miniGroupJoin);
        Animation animation = new TranslateAnimation(0, -1200,0, 0); //May need to check the direction you want.
        animation.setDuration(700);
        animation.setFillAfter(true);

        animation.setAnimationListener(new AnimationListenerAdapter() {

            @Override
            public void onAnimationEnd(Animation animation) {
                View slotLabel = card.findViewById(R.id.group_slot_label);
                slotLabel.setVisibility(View.INVISIBLE);
            }
        });

        groupJoinView.startAnimation(animation);
    }

    /**
     * Hide Member List from Card
     * @param card View
     */
    private void hideMemberList(final View card) {
        View capacityView = card.findViewById(R.id.capacity);

        final RecyclerView membersView = card.findViewById(R.id.membersView);
        Animation animation = new TranslateAnimation(0, 1200,0, 0); //May need to check the direction you want.
        animation.setDuration(700);
        animation.setFillAfter(true);

        membersView.startAnimation(animation);
        capacityView.startAnimation(animation);
        capacityView.setVisibility(GONE);
        membersView.setVisibility(GONE);
    }

    private void bindGroupToGroupJoinView(View groupJoinView, GroupEntryOpportunity groupEntryOpportunity) {
        TextView slotLabelView = groupJoinView.findViewById(R.id.group_slot_label);
        slotLabelView.setText("Draw Slot - " + groupEntryOpportunity.getSlotNumber());
    }
}
