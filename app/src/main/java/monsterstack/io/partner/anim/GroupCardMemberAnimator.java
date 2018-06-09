package monsterstack.io.partner.anim;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
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
    private static final Integer DEFAULT_FADE_OUT_DURATION = 300;
    private static final Integer DEFAULT_FADE_IN_DURATION = 250;
    private static final Integer DEFAULT_SCALE_UP_DURATION = 600;
    private static final Integer DEFAULT_SCALE_DOWN_DURATION = 500;
    private static final Integer DEFAULT_SCALE_UP_START_X = 0;
    private static final Integer DEFAULT_SCALE_UP_START_Y = 0;
    private static final Integer DEFAULT_SCALE_UP_END_X = 1;
    private static final Integer DEFAULT_SCALE_UP_END_Y = 1;

    private static final Integer DEFAULT_SCALE_DOWN_START_X = 1;
    private static final Integer DEFAULT_SCALE_DOWN_START_Y = 1;
    private static final Integer DEFAULT_SCALE_DOWN_END_X = 0;
    private static final Integer DEFAULT_SCALE_DOWN_END_Y = 0;


    private static final Float DEFAULT_SCALE_PIVOT_X = 0.5f;
    private static final Float DEFAULT_SCALE_PIVOT_Y = 0.5f;

    private static final Float TRANSLATE_SLIDE_UP_START_Y = 0f;
    private static final Float TRANSLATE_SLIDE_UP_END_Y = -450f;

    private static final Float FADE_IN_END = 1.0f;
    private static final Float FADE_OUT_END = 0f;

    @BindView(R.id.membersView)
    RecyclerView membersView;
    @BindView(R.id.miniMemberAvatar)
    LinearLayout memberAvatarView;
    @BindView(R.id.miniMemberDetails)
    LinearLayout memberDetailsView;
    @BindView(R.id.member_close_button)
    View closeButton;
    @BindView(R.id.member_slot_value)
    TextView slotValue;
    @BindView(R.id.capacity)
    View capacityView;

    @BindView(R.id.group_details_container)
    FrameLayout groupViewContainer;

    private CardView cardView;

    public GroupCardMemberAnimator(CardView root, AnimationOptions animationOptions) {
        super(animationOptions);
        ButterKnife.bind(this, root);

        this.cardView = root;
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
        Animation animation = new ScaleAnimation(
                DEFAULT_SCALE_UP_START_X,
                DEFAULT_SCALE_UP_END_X,
                DEFAULT_SCALE_UP_START_Y,
                DEFAULT_SCALE_UP_END_Y,
                Animation.RELATIVE_TO_SELF,
                DEFAULT_SCALE_PIVOT_X,
                Animation.RELATIVE_TO_SELF,
                DEFAULT_SCALE_PIVOT_Y);

        animation.setDuration(DEFAULT_SCALE_UP_DURATION);
        membersView.setVisibility(View.VISIBLE);

        animation.setAnimationListener(new AnimationListenerAdapter() {

            @Override
            public void onAnimationEnd(Animation animation) {
                capacityView.clearAnimation();
                capacityView.setVisibility(VISIBLE);
            }
        });

        ObjectAnimator animator = ObjectAnimator.ofFloat(this.membersView, "alpha",FADE_IN_END);
        animator.setDuration(DEFAULT_FADE_IN_DURATION);
        animator.setInterpolator(new LinearInterpolator());
        animator.start();
        this.membersView.startAnimation(animation);
    }

    private void scaleDownMemberList() {
        capacityView.setVisibility(View.GONE);

        Animation animation = new ScaleAnimation(
                DEFAULT_SCALE_DOWN_START_X,
                DEFAULT_SCALE_DOWN_END_X,
                DEFAULT_SCALE_DOWN_START_Y,
                DEFAULT_SCALE_DOWN_END_Y,
                Animation.RELATIVE_TO_SELF,
                DEFAULT_SCALE_PIVOT_X,
                Animation.RELATIVE_TO_SELF,
                DEFAULT_SCALE_PIVOT_Y);
        animation.setDuration(DEFAULT_SCALE_DOWN_DURATION);


        animation.setAnimationListener(new AnimationListenerAdapter() {

            @Override
            public void onAnimationEnd(Animation animation) {
                membersView.clearAnimation();
                membersView.setVisibility(View.GONE);
            }
        });

        ObjectAnimator animator = ObjectAnimator.ofFloat(this.membersView, "alpha",FADE_OUT_END);
        animator.setDuration(DEFAULT_FADE_OUT_DURATION);
        animator.setInterpolator(new LinearInterpolator());
        animator.start();
        this.membersView.startAnimation(animation);
    }

    private void scaleUpMemberDetails(final Member member) {
        bindMemberToMemberAvatarView(this.memberAvatarView, member);
        bindMemberToMemberDetailView(this.memberDetailsView, member);
        ObjectAnimator slideUpAnimation = ObjectAnimator.ofFloat(groupViewContainer,
                "translationY", TRANSLATE_SLIDE_UP_END_Y);

        slideUpAnimation.setDuration(getAnimationOptions().getExpandDuration());
        slideUpAnimation.setInterpolator(getAnimationOptions().getInterpolator());

        capacityView.setVisibility(View.GONE);
        closeButton.setVisibility(VISIBLE);

        slideUpAnimation.start();

        this.memberAvatarView.setVisibility(VISIBLE);
        this.memberDetailsView.setVisibility(VISIBLE);
        Animation animation = new ScaleAnimation(
                DEFAULT_SCALE_UP_START_X,
                DEFAULT_SCALE_UP_END_X,
                DEFAULT_SCALE_UP_START_Y,
                DEFAULT_SCALE_UP_END_Y,
                Animation.RELATIVE_TO_SELF,
                DEFAULT_SCALE_PIVOT_X,
                Animation.RELATIVE_TO_SELF,
                DEFAULT_SCALE_PIVOT_Y);

        animation.setInterpolator(new AccelerateDecelerateInterpolator());

        animation.setDuration(getAnimationOptions().getExpandDuration());

        // When done animating, apply click listener to incoming view
        animation.setAnimationListener(new AnimationListenerAdapter() {

            @Override
            public void onAnimationEnd(Animation animation) {
                getAnimationOptions().getViewAnimatedListener().onViewExpanded();
                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        animateOut();
                    }
                });

                capacityView.clearAnimation();
                capacityView.setVisibility(GONE);
            }
        });

        this.memberAvatarView.startAnimation(animation);
        this.memberDetailsView.startAnimation(animation);
    }

    private void scaleDownMemberDetails() {
        ObjectAnimator slideDownAnimation = ObjectAnimator.ofFloat(groupViewContainer,
                "translationY", TRANSLATE_SLIDE_UP_START_Y);
        slideDownAnimation.setDuration(getAnimationOptions().getCollapseDuration());
        slideDownAnimation.setInterpolator(getAnimationOptions().getInterpolator());
        slideDownAnimation.start();

        Animation animation = new ScaleAnimation(
                DEFAULT_SCALE_DOWN_START_X,
                DEFAULT_SCALE_DOWN_END_X,
                DEFAULT_SCALE_DOWN_START_Y,
                DEFAULT_SCALE_DOWN_END_Y,
                Animation.RELATIVE_TO_SELF,
                DEFAULT_SCALE_PIVOT_X,
                Animation.RELATIVE_TO_SELF,
                DEFAULT_SCALE_PIVOT_Y);

        animation.setDuration(getAnimationOptions().getCollapseDuration());
        animation.setInterpolator(getAnimationOptions().getInterpolator());

        closeButton.setVisibility(GONE);

        animation.setAnimationListener(new AnimationListenerAdapter() {
            @Override
            public void onAnimationEnd(Animation animation) {
                memberDetailsView.clearAnimation();
                memberAvatarView.clearAnimation();
                memberDetailsView.setVisibility(View.INVISIBLE);
                memberAvatarView.setVisibility(View.INVISIBLE);

                getAnimationOptions().getViewAnimatedListener().onViewCollapsed();
            }
        });
        this.memberDetailsView.startAnimation(animation);
        this.memberAvatarView.startAnimation(animation);
    }

    /**
     * Bind Member Data to member details
     * @param memberAvatarView View
     * @param member    Member
     */
    private void bindMemberToMemberAvatarView(final View memberAvatarView,
                                               final Member member) {
        AvatarView avatarView = memberAvatarView.findViewById(R.id.memberAvatarView);
        Button closeButtonView = memberAvatarView.findViewById(R.id.member_close_button);

        Context context = memberAvatarView.getContext();

        avatarView.setUser(new User(member.getFullName(), member.getAvatar(), R.color.colorAccent));

        slotValue.setText("Draw Slot - " + member.getSlotNumber());
    }

    private void bindMemberToMemberDetailView(final View memberDetailsView, final Member member) {
        Context context = memberDetailsView.getContext();

        TextView fullNameView = memberDetailsView.findViewById(R.id.memberFullName);
        UserSessionManager sessionManager = new UserSessionManager(context);
        AuthenticatedUser authenticatedUser = sessionManager.getUserDetails();

        String fullName = member.getFullName();
        if(authenticatedUser.getFullName().equalsIgnoreCase(member.getFullName())) {
            fullName = "You";
        }
        fullNameView.setText(fullName);

    }
}
