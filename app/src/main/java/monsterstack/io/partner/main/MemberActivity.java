package monsterstack.io.partner.main;

import android.support.v7.app.AppCompatActivity;

import monsterstack.io.partner.R;
import monsterstack.io.partner.common.BasicActivity;

public class MemberActivity extends BasicActivity {
    @Override
    public int getContentView() {
        return R.layout.activity_member;
    }

    @Override
    public AppCompatActivity getActivity() {
        return this;
    }

    @Override
    public int getActionTitle() {
        return R.string.members_title;
    }

    @Override
    public void onBackPressed() {
        finishAfterTransition();
    }
}
