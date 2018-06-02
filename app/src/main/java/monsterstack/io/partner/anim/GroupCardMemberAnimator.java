package monsterstack.io.partner.anim;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import monsterstack.io.api.UserSessionManager;
import monsterstack.io.api.resources.AuthenticatedUser;
import monsterstack.io.avatarview.AvatarView;
import monsterstack.io.avatarview.User;
import monsterstack.io.partner.R;
import monsterstack.io.partner.domain.Member;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class GroupCardMemberAnimator {

    public void animateIn(CardView card, Member member) {
        hideMemberList(card);
        showMemberDetails(card, member);
    }

    public void animateOut(CardView card) {
        showMemberList(card);
        hideMemberDetails(card);
    }


    /**
     * Show Member Details on Card
     * @param card  View
     * @param member    Member
     */
    private void showMemberDetails(final View card, final Member member) {
        // Bind member to entering view
        final RelativeLayout memberDetailsView = card.findViewById(R.id.miniMemberDetails);
        bindMemberToMemberDetailsView(memberDetailsView, member);

        memberDetailsView.setVisibility(VISIBLE);
        View slotLabel = card.findViewById(R.id.member_slot_label);
        slotLabel.setVisibility(VISIBLE);

        Animation animation = new TranslateAnimation(-900, 0, 0, 0);
        animation.setDuration(1000);
        animation.setFillAfter(true);

        // When done animating, apply click listener to incoming view
        animation.setAnimationListener(new AnimationListenerAdapter() {

            @Override
            public void onAnimationEnd(Animation animation) {
                View slotLabel = card.findViewById(R.id.member_slot_label);
                slotLabel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showMemberList(card);
                        hideMemberDetails(card);
                    }
                });
            }
        });

        memberDetailsView.startAnimation(animation);
    }


    private void hideMemberDetails(final View card) {
        final RelativeLayout memberDetailsView = card.findViewById(R.id.miniMemberDetails);

        Animation animation = new TranslateAnimation(0, -900, 0, 0);
        animation.setDuration(500);
        animation.setFillAfter(true);

        animation.setAnimationListener(new AnimationListenerAdapter() {

            @Override
            public void onAnimationEnd(Animation animation) {
                TextView slotLabelView = memberDetailsView.findViewById(R.id.member_slot_label);
                slotLabelView.setVisibility(View.INVISIBLE);
            }
        });

        memberDetailsView.startAnimation(animation);
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

    /**
     * Bind Member Data to member details
     * @param memberDetailsView View
     * @param member    Member
     */
    private void bindMemberToMemberDetailsView(final View memberDetailsView,
                                               final Member member) {
        AvatarView avatarView = memberDetailsView.findViewById(R.id.memberAvatarView);
        TextView fullNameView = memberDetailsView.findViewById(R.id.memberFullName);
        TextView slotLabelView = memberDetailsView.findViewById(R.id.member_slot_label);

        Context context = memberDetailsView.getContext();

        UserSessionManager sessionManager = new UserSessionManager(context);
        AuthenticatedUser authenticatedUser = sessionManager.getUserDetails();

        String fullName = member.getFullName();
        if(authenticatedUser.getFullName().equalsIgnoreCase(member.getFullName())) {
            fullName = "You";
        }

        avatarView.setUser(new User(member.getFullName(), member.getAvatar(), R.color.colorAccent));
        fullNameView.setText(fullName);

        slotLabelView.setText("Draw Slot - " + member.getSlotNumber());
    }


}
