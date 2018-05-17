package monsterstack.io.partner.main.details;

import android.support.v7.app.AppCompatActivity;

import monsterstack.io.partner.R;
import monsterstack.io.partner.common.BasicActivity;

public class GroupDetailsActivity extends BasicActivity {
    @Override
    public int getContentView() {
        return R.layout.group_details;
    }

    @Override
    public AppCompatActivity getActivity() {
        return this;
    }

    @Override
    public int getActionTitle() {
        return R.string.group_details_title;
    }
}
