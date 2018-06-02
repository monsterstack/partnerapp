package monsterstack.io.partner.anim;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
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

    @BindView(R.id.group_details_container)
    FrameLayout groupViewContainer;

    public GroupCardMemberAnimator(CardView root, AnimationOptions animationOptions) {
        super(animationOptions);
        ButterKnife.bind(this, root);
    }

    @Override
    public void animateIn(Member member) {
        scaleDownMemberList();
        scaleUpMemberDetails(member);
    }

    @Override
    public void animateOut() {
        scaleDownMemberDetails();
        scaleUpMemberList();
    }

    private void scaleUpMemberList() {
        Animation animation = new ScaleAnimation(0,1,0,1,
                Animation.RELATIVE_TO_SELF, (float)0.5, Animation.RELATIVE_TO_SELF, (float)0.5);
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
                Animation.RELATIVE_TO_SELF, (float)0.5, Animation.RELATIVE_TO_SELF, (float)0.5);
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

        ObjectAnimator slideUpAnimation = ObjectAnimator.ofFloat(groupViewContainer, "translationY", -450f);
        slideUpAnimation.setDuration(getAnimationOptions().getExpandDuration());
        slideUpAnimation.setInterpolator(getAnimationOptions().getInterpolator());

        slotLabel.setVisibility(VISIBLE);

        slideUpAnimation.start();

        this.memberDetailsView.setVisibility(VISIBLE);
        Animation animation = new ScaleAnimation(0,1,0,1,
                Animation.RELATIVE_TO_SELF, (float)0.5, Animation.RELATIVE_TO_SELF, (float)0.5);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());

        animation.setDuration(350);

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

        this.memberDetailsView.startAnimation(animation);
    }

    private void scaleDownMemberDetails() {
        ObjectAnimator slideDownAnimation = ObjectAnimator.ofFloat(groupViewContainer, "translationY", 0f);
        slideDownAnimation.setDuration(getAnimationOptions().getCollapseDuration());
        slideDownAnimation.setInterpolator(getAnimationOptions().getInterpolator());
        slideDownAnimation.start();

        Animation animation = new ScaleAnimation(1,0,1,0,
                Animation.RELATIVE_TO_SELF, (float)0.5, Animation.RELATIVE_TO_SELF, (float)0.5);
        animation.setDuration(350);
        animation.setInterpolator(getAnimationOptions().getInterpolator());

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
