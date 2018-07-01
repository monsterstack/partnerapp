package monsterstack.io.partner.common;

import android.view.View;
import android.view.ViewGroup;

public interface HasDimmingSupport {
    static final Integer LIGHTER = 0;
    static final Integer DARKER = 150;

    default void dim(ViewGroup view) {
        view.getForeground().setAlpha(DARKER);

        disableEnableControls(false, view);
    }

    default void brighten(ViewGroup view) {
        view.getForeground().setAlpha(LIGHTER);

        disableEnableControls(true, view);
    }

    default void disableEnableControls(boolean enable, ViewGroup viewGroup) {
        for (int i = 0; i < viewGroup.getChildCount(); i++){
            View child = viewGroup.getChildAt(i);
            child.setEnabled(enable);
            if (child instanceof ViewGroup){
                disableEnableControls(enable, (ViewGroup)child);
            }
        }
    }
}
