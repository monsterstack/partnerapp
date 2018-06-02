package monsterstack.io.partner.anim;

import android.view.View;

public abstract class ViewAnimator<V extends View, M> {
    public abstract void animateIn(M data);
    public abstract void animateOut();
}
