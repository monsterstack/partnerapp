package monsterstack.io.partner.main;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import monsterstack.io.api.listeners.OnResponseListener;
import monsterstack.io.api.resources.HttpError;
import monsterstack.io.partner.Application;
import monsterstack.io.partner.R;
import monsterstack.io.partner.common.BasicActivity;

import monsterstack.io.partner.domain.Member;
import monsterstack.io.partner.domain.Transaction;
import monsterstack.io.partner.domain.TransactionType;
import monsterstack.io.partner.main.control.MemberControl;
import monsterstack.io.partner.main.presenter.MemberPresenter;

public class MemberActivity extends BasicActivity implements MemberControl {
    public static final String EXTRA_MEMBER = "member";
    private Member member;

    private MemberPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = getPresenterFactory().getMemberPresenter(this, this);

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
    public void injectDependencies(BasicActivity basicActivity) {
        super.injectDependencies(basicActivity);
        ((Application) getApplication()).component().inject(this);
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


    public void findTransactionsForMemberGroup(OnResponseListener<Transaction[], HttpError> listener) {
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
