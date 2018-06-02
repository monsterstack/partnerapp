package monsterstack.io.partner.anim;


import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AnimationOptions {
    private Integer expandDuration;
    private Integer collapseDuration;

    private Interpolator interpolator;

    public static AnimationOptions options(Integer expandDuration, Integer collapseDuration) {
        return new AnimationOptions(expandDuration, collapseDuration, new AccelerateDecelerateInterpolator());
    }
}
