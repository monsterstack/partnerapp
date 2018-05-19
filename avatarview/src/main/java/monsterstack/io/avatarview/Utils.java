package monsterstack.io.avatarview;

import android.content.res.Resources;
import android.util.DisplayMetrics;

public class Utils {
    public static int dpToPixel(int dp, Resources resources) {
        return convertDpToPx(dp, resources);
    }


    private static int convertDpToPx(int dp, Resources resources){
        return Math.round(dp*(resources.getDisplayMetrics().xdpi/ DisplayMetrics.DENSITY_DEFAULT));

    }
}
