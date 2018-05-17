package monsterstack.io.partner.main.details;

import android.support.v7.app.AppCompatActivity;

import monsterstack.io.partner.R;
import monsterstack.io.partner.common.BasicActivity;

public class TransactionsActivity extends BasicActivity {
    @Override
    public int getContentView() {
        return R.layout.activity_transactions;
    }

    @Override
    public AppCompatActivity getActivity() {
        return this;
    }

    @Override
    public int getActionTitle() {
        return R.string.transactions_title;
    }
}
