package monsterstack.io.partner.anim;


import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

import lombok.Data;
import monsterstack.io.partner.adapter.ViewAnimatedListener;

@Data
public class AnimationOptions {
    private Integer expandDuration;
    private Integer collapseDuration;

    private Interpolator interpolator;

    private ViewAnimatedListener viewAnimatedListener;

    AnimationOptions(Integer expandDuration, Integer collapseDuration, Interpolator interpolator) {
        this.expandDuration = expandDuration;
        this.collapseDuration = collapseDuration;
        this.interpolator = interpolator;
    }

    public AnimationOptions onViewAnimatedListener(ViewAnimatedListener viewAnimatedListener) {
        this.viewAnimatedListener = viewAnimatedListener;
        return this;
    }

    public static AnimationOptions options(Integer expandDuration, Integer collapseDuration) {
        return new AnimationOptions(expandDuration, collapseDuration, new AccelerateDecelerateInterpolator());
    }
}
