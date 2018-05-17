package monsterstack.io.partner.menu;

import android.view.View;

public interface ClosableActivity {
    View.OnClickListener getCloseListener();
    int getContentView();
}
