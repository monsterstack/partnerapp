package monsterstack.io.partner.main.presenter;

import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.View;
import android.widget.ProgressBar;

import java.util.Map;
import java.util.Optional;

import butterknife.BindView;
import monsterstack.io.api.listeners.OnResponseListener;
import monsterstack.io.api.resources.HttpError;
import monsterstack.io.partner.R;
import monsterstack.io.partner.adapter.TransactionArrayAdapter;
import monsterstack.io.partner.common.HasProgressBarSupport;
import monsterstack.io.partner.common.presenter.Presenter;
import monsterstack.io.partner.domain.Transaction;
import monsterstack.io.partner.main.control.GroupTransactionsControl;

public class GroupTransactionsPresenter implements Presenter<GroupTransactionsControl>, HasProgressBarSupport {

    @BindView(R.id.groupTransactions)
    RecyclerView groupTransactions;

    @BindView(R.id.progressbar)
    ProgressBar progressBar;

    private GroupTransactionsControl control;

    @Override
    public Presenter<GroupTransactionsControl> present(Optional<Map> metadata) {
        control.findTransactionsForGroup(new OnResponseListener<Transaction[], HttpError>() {
            @Override
            public void onResponse(Transaction[] transactions, HttpError httpError) {

                groupTransactions.setLayoutManager(new LinearLayoutManager(control.getContext()));
                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(control.getContext(),
                        DividerItemDecoration.VERTICAL);
                groupTransactions.addItemDecoration(dividerItemDecoration);
                groupTransactions.setAdapter(new TransactionArrayAdapter(control.getContext(), transactions));
            }
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
}
