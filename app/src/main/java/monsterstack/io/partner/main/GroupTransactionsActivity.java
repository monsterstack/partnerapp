package monsterstack.io.partner.main;

import android.support.v7.app.AppCompatActivity;

import monsterstack.io.partner.R;
import monsterstack.io.partner.common.BasicActivity;

public class GroupTransactionsActivity extends BasicActivity {
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
}
