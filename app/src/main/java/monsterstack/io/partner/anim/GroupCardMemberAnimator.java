package monsterstack.io.partner.anim;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import monsterstack.io.api.UserSessionManager;
import monsterstack.io.api.resources.AuthenticatedUser;
import monsterstack.io.avatarview.AvatarView;
import monsterstack.io.avatarview.User;
import monsterstack.io.partner.R;
import monsterstack.io.partner.domain.Member;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class GroupCardMemberAnimator extends ViewAnimator<CardView, Member> {
    @BindView(R.id.membersView)
    RecyclerView membersView;
    @BindView(R.id.miniMemberDetails)
    RelativeLayout memberDetailsView;
    @BindView(R.id.member_slot_label)
    View slotLabel;
    @BindView(R.id.capacity)
    View capacityView;

    public GroupCardMemberAnimator(CardView root) {
        ButterKnife.bind(this, root);
    }

    public void scaleUp(Member member) {
        scaleDownMemberList();
        scaleUpMemberDetails(member);
    }

    public void scaleDown() {
        scaleDownMemberDetails();
        scaleUpMemberList();
    }

    @Override
    public void animateIn(Member member) {
        hideMemberList();
        showMemberDetails(member);
    }

    @Override
    public void animateOut() {
        showMemberList();
        hideMemberDetails();
    }

    private void scaleUpMemberList() {
        Animation animation = new ScaleAnimation(0,1,0,1,
                Animation.RELATIVE_TO_PARENT, (float)0.5, Animation.RELATIVE_TO_PARENT, (float)0.5);
        animation.setDuration(1000);
        membersView.setVisibility(View.VISIBLE);

        animation.setAnimationListener(new AnimationListenerAdapter() {

            @Override
            public void onAnimationEnd(Animation animation) {
                capacityView.setVisibility(VISIBLE);
            }
        });
        this.membersView.startAnimation(animation);
    }

    private void scaleDownMemberList() {
        Animation animation = new ScaleAnimation(1,0,1,0,
                Animation.RELATIVE_TO_PARENT, (float)0.5, Animation.RELATIVE_TO_PARENT, (float)0.5);
        animation.setDuration(800);

        capacityView.setVisibility(View.INVISIBLE);

        animation.setAnimationListener(new AnimationListenerAdapter() {

            @Override
            public void onAnimationEnd(Animation animation) {
                membersView.setVisibility(View.GONE);
            }
        });

        this.membersView.startAnimation(animation);
    }

    private void scaleUpMemberDetails(final Member member) {
        bindMemberToMemberDetailsView(this.memberDetailsView, member);
        this.slotLabel.setVisibility(VISIBLE);
        this.memberDetailsView.setVisibility(VISIBLE);
        Animation animation = new ScaleAnimation(0,1,0,1,
                Animation.RELATIVE_TO_SELF, (float)0.5, Animation.RELATIVE_TO_SELF, (float)0.5);
        animation.setDuration(1000);

        // When done animating, apply click listener to incoming view
        animation.setAnimationListener(new AnimationListenerAdapter() {

            @Override
            public void onAnimationEnd(Animation animation) {
                slotLabel.setVisibility(VISIBLE);
                slotLabel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        scaleDown();
                    }
                });
            }
        });

        this.memberDetailsView.startAnimation(animation);
    }

    private void scaleDownMemberDetails() {
        Animation animation = new ScaleAnimation(1,0,1,0,
                Animation.RELATIVE_TO_SELF, (float)0.5, Animation.RELATIVE_TO_SELF, (float)0.5);
        animation.setDuration(800);
        slotLabel.setVisibility(GONE);

        animation.setAnimationListener(new AnimationListenerAdapter() {
            @Override
            public void onAnimationEnd(Animation animation) {
                memberDetailsView.setVisibility(View.INVISIBLE);
            }
        });
        this.memberDetailsView.startAnimation(animation);
    }

    /**
     * Show Member Details on Card
     * @param member    Member
     */
    private void showMemberDetails(final Member member) {
        // Bind member to entering view
        bindMemberToMemberDetailsView(this.memberDetailsView, member);

        this.memberDetailsView.setVisibility(VISIBLE);
        this.slotLabel.setVisibility(VISIBLE);

        Animation animation = new TranslateAnimation(-900, 0, 0, 0);
        animation.setDuration(1000);
        animation.setFillAfter(true);

        // When done animating, apply click listener to incoming view
        animation.setAnimationListener(new AnimationListenerAdapter() {

            @Override
            public void onAnimationEnd(Animation animation) {
                slotLabel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        animateOut();
                    }
                });
            }
        });

        memberDetailsView.startAnimation(animation);
    }


    private void hideMemberDetails() {
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

        this.memberDetailsView.startAnimation(animation);
    }

    private void showMemberList() {
        this.membersView.setVisibility(VISIBLE);
        this.capacityView.setVisibility(VISIBLE);
        Animation animation = new TranslateAnimation(1200, 0,0, 0); //May need to check the direction you want.
        animation.setDuration(1000);
        animation.setFillAfter(true);

        this.membersView.startAnimation(animation);
        this.capacityView.startAnimation(animation);
    }

    /**
     * Hide Member List from Card
     */
    private void hideMemberList() {
        Animation animation = new TranslateAnimation(0, 1200,0, 0); //May need to check the direction you want.
        animation.setDuration(700);
        animation.setFillAfter(true);

        this.membersView.startAnimation(animation);
        this.capacityView.startAnimation(animation);
        this.capacityView.setVisibility(GONE);
        this.membersView.setVisibility(GONE);
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
