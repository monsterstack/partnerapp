package monsterstack.io.partner.main.presenter.impl;

import android.content.Context;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.View;
import android.widget.ProgressBar;

import java.util.Map;
import java.util.Optional;

import butterknife.BindView;
import monsterstack.io.partner.R;
import monsterstack.io.partner.adapter.TransactionArrayAdapter;
import monsterstack.io.partner.common.presenter.Presenter;
import monsterstack.io.partner.domain.Transaction;
import monsterstack.io.partner.main.control.GroupTransactionsControl;
import monsterstack.io.partner.main.presenter.GroupTransactionsPresenter;

public class GroupTransactionsPresenterImpl implements GroupTransactionsPresenter {

    @BindView(R.id.groupTransactions)
    RecyclerView groupTransactions;

    @BindView(R.id.progressbar)
    ProgressBar progressBar;

    private GroupTransactionsControl control;

    @Override
    public Presenter<GroupTransactionsControl> present(Optional<Map> metadata) {
        control.findTransactionsForGroup((transactions, httpError) -> {

            groupTransactions.setLayoutManager(new LinearLayoutManager(control.getContext()));
            DividerItemDecoration dividerItemDecoration = createDividerDecoration(control.getContext());
            groupTransactions.addItemDecoration(dividerItemDecoration);
            groupTransactions.setAdapter(createTransactionArrayAdapter(control.getContext(), transactions));
        });

        return this;
    }

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public Presenter<GroupTransactionsControl> bind(GroupTransactionsControl control) {
        this.control = control;
        return this;
    }

    @Override
    public GroupTransactionsControl getControl() {
        return control;
    }

    protected DividerItemDecoration createDividerDecoration(Context context) {
        return new DividerItemDecoration(context,
                DividerItemDecoration.VERTICAL);
    }

    protected TransactionArrayAdapter createTransactionArrayAdapter(Context context, Transaction[] transactions) {
        return new TransactionArrayAdapter(context, transactions);
    }
}
