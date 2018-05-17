package monsterstack.io.partner.common;

import android.view.View;

public class ToolbarModel {

    private String title;
    private View.OnClickListener onCloseListener;
    private View.OnClickListener onBackListener;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public View.OnClickListener getOnCloseListener() {
        return onCloseListener;
    }

    public void setOnCloseListener(View.OnClickListener onCloseListener) {
        this.onCloseListener = onCloseListener;
    }

    public View.OnClickListener getOnBackListener() {
        return onBackListener;
    }

    public void setOnBackListener(View.OnClickListener onBackListener) {
        this.onBackListener = onBackListener;
    }
}
