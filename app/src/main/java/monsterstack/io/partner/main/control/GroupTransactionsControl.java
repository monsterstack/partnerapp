package monsterstack.io.partner.main.control;

import monsterstack.io.api.listeners.OnResponseListener;
import monsterstack.io.api.resources.HttpError;
import monsterstack.io.partner.common.Control;
import monsterstack.io.partner.domain.Transaction;

public interface GroupTransactionsControl extends Control {
    void findTransactionsForGroup(OnResponseListener<Transaction[], HttpError> listener);
}
