package monsterstack.io.partner.anim;

import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

public abstract class ViewAnimator<V extends View, M> {
    private AnimationOptions animationOptions;

    ViewAnimator(AnimationOptions animationOptions) {
        this.animationOptions = animationOptions;


        if (null == animationOptions.getExpandDuration()) {
            animationOptions.setExpandDuration(500);
        }

        if (null == animationOptions.getCollapseDuration()) {
            animationOptions.setCollapseDuration(400);
        }

        if (null == animationOptions.getInterpolator()) {
            animationOptions.setInterpolator(new AccelerateDecelerateInterpolator());
        }
    }

    public abstract void animateIn(M data);
    public abstract void animateOut();

    AnimationOptions getAnimationOptions() {
        return animationOptions;
    }
}
