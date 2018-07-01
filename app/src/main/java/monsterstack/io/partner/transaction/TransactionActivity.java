package monsterstack.io.partner.transaction;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import monsterstack.io.partner.R;
import monsterstack.io.partner.common.BasicActivity;
import monsterstack.io.partner.transaction.control.TransactionControl;
import monsterstack.io.partner.transaction.presenter.TransactionPresenter;

public class TransactionActivity extends BasicActivity implements TransactionControl {
    private TransactionPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new TransactionPresenter(this);

        ButterKnife.bind(presenter, this);
    }

    @Override
    public int getContentView() {
        return R.layout.transaction;
    }

    @Override
    public AppCompatActivity getActivity() {
        return this;
    }

    @Override
    public int getActionTitle() {
        return R.string.transactions_title;
    }

    @Override
    public Context getContext() {
        return this;
    }
}
