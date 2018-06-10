package monsterstack.io.partner.main;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import butterknife.ButterKnife;
import monsterstack.io.partner.R;
import monsterstack.io.partner.common.BasicActivity;
import monsterstack.io.partner.domain.Member;
import monsterstack.io.partner.main.control.MemberControl;
import monsterstack.io.partner.main.presenter.MemberPresenter;

public class MemberActivity extends BasicActivity implements MemberControl {
    public static final String EXTRA_MEMBER = "member";
    private Member member;

    private MemberPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new MemberPresenter(this);
        ButterKnife.bind(presenter, this);

        this.member = (Member)getIntent().getSerializableExtra(EXTRA_MEMBER);

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("member", this.member);
        presenter.present(Optional.<Map>of(metadata));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.close_action, menu);

        MenuItem menuItem = menu.findItem(R.id.modal_close_button);
        menuItem.setOnMenuItemClickListener(getCloseListener());
        return true;
    }

    @Override
    public boolean displayHomeAsUp() {
        return false;
    }

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
        return R.string.member_title;
    }

    @Override
    public void onBackPressed() {
        finishAfterTransition();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public MenuItem.OnMenuItemClickListener getCloseListener() {
        return new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                MemberActivity.this.finish();
                ((Activity)getContext()).overridePendingTransition(R.anim.hold, R.anim.slide_down);
                return false;
            }
        };
    }
}
