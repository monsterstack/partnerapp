package monsterstack.io.partner.common;

import android.graphics.PorterDuff;
import android.widget.ProgressBar;
public interface HasProgressBarSupport {
    void hideProgressBar();
    void showProgressBar();

    default void changeColor(ProgressBar progressBar, int color) {
        progressBar.getProgressDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
    }
}
