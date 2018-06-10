package monsterstack.io.partner.main.control;

import android.view.MenuItem;

import monsterstack.io.partner.common.Control;

public interface MemberControl extends Control {

    MenuItem.OnMenuItemClickListener getCloseListener();
}
