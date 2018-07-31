package monsterstack.io.partner.main;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.time.Instant;
import java.util.Optional;

import monsterstack.io.api.listeners.OnResponseListener;
import monsterstack.io.api.resources.HttpError;
import monsterstack.io.partner.Application;
import monsterstack.io.partner.R;
import monsterstack.io.partner.common.BasicActivity;
import monsterstack.io.partner.domain.Transaction;
import monsterstack.io.partner.domain.TransactionType;
import monsterstack.io.partner.main.control.GroupTransactionsControl;
import monsterstack.io.partner.main.presenter.GroupTransactionsPresenter;

public class GroupTransactionsActivity extends BasicActivity implements GroupTransactionsControl {
    public static final String EXTRA_GROUP = "group";

    private GroupTransactionsPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = getPresenterFactory().getGroupTransactionsPresenter(this, this);

        presenter.present(Optional.empty());
    }

    @Override
    public void injectDependencies(BasicActivity basicActivity) {
        super.injectDependencies(basicActivity);
        ((Application) getApplication()).component().inject(this);
    }

    @Override
    public int getContentView() {
        return R.layout.group_transactions;
    }

    @Override
    public AppCompatActivity getActivity() {
        return this;
    }

    @Override
    public int getActionTitle() {
        return R.string.group_transactions;
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void findTransactionsForGroup(OnResponseListener<Transaction[], HttpError> listener) {
        Transaction[] transactions = new Transaction[] {
                new Transaction(Instant.now(), TransactionType.DEBIT, 50.00 ),
                new Transaction(Instant.now(), TransactionType.CREDIT, 5.00),
                new Transaction(Instant.now(), TransactionType.DEBIT, 23.45),
                new Transaction(Instant.now(), TransactionType.DEBIT, 50.00),
                new Transaction(Instant.now(), TransactionType.DEBIT, 5.00)
        };

        listener.onResponse(transactions, null);
    }
}
