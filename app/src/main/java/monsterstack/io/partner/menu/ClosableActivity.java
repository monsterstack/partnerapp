package monsterstack.io.partner.menu;

import android.view.MenuItem;


public interface ClosableActivity {
    MenuItem.OnMenuItemClickListener getCloseListener();
    int getContentView();
}
