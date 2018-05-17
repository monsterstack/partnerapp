package monsterstack.io.partner.utils;

import android.app.ActivityOptions;
import android.content.Context;
import android.os.Bundle;

import monsterstack.io.partner.R;

public class NavigationUtils {

    public static Bundle enterStageRightBundle(Context context) {
        return ActivityOptions.makeCustomAnimation(context, R.anim.slide_left, R.anim.slide_right).toBundle();
    }

    public static Bundle exitStageLeftBundle(Context context) {
        return ActivityOptions.makeCustomAnimation(context,
                R.anim.back_slide_right, R.anim.back_slide_left).toBundle();
    }

    public static Bundle enterStageBottom(Context context) {
        return ActivityOptions.makeCustomAnimation(context, R.anim.slide_up, R.anim.hold).toBundle();
    }

    public static Bundle exitStageBottom(Context context) {
        return ActivityOptions.makeCustomAnimation(context, R.anim.hold, R.anim.slide_down).toBundle();
    }
}
