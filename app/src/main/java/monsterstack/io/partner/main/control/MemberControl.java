package monsterstack.io.partner.main.control;

import android.view.MenuItem;

import monsterstack.io.api.listeners.OnResponseListener;
import monsterstack.io.api.resources.HttpError;
import monsterstack.io.partner.common.Control;
import monsterstack.io.partner.domain.Transaction;

public interface MemberControl extends Control {

    MenuItem.OnMenuItemClickListener getCloseListener();

    void findTransactionsForMemberGroup(OnResponseListener<Transaction[], HttpError> listener);
}
