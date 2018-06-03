package monsterstack.io.streamview;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class PausingViewPager extends ViewPager {
    private boolean pagingEnabled = true;

    public PausingViewPager(Context context) {
        super(context);
    }

    public PausingViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setPaused(boolean paused) {
        this.pagingEnabled = !paused;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return pagingEnabled && super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return pagingEnabled && super.onTouchEvent(event);
    }

}
