package monsterstack.io.partner.main.details;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import butterknife.BindView;
import butterknife.ButterKnife;
import monsterstack.io.partner.R;
import monsterstack.io.partner.adapter.MemberArrayAdapter;
import monsterstack.io.partner.common.BasicActivity;
import monsterstack.io.partner.domain.Member;

public class MembersActivity extends BasicActivity {
    @BindView(R.id.members_list)
    ListView listView;
    @BindView(R.id.members_content)
    SwipeRefreshLayout mainContent;

    // Array of strings...
    private Member[] memberArray = {
            new Member("Zachary", "Rote", 10, Member.avatars[0]),
            new Member("Harrisen", "Rote", 5, Member.avatars[1]),
            new Member("Arabella", "Rote", 4, Member.avatars[2])
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);

        listView.setAdapter(new MemberArrayAdapter(getApplicationContext(), memberArray ));

        mainContent.setProgressViewOffset(true, 100, 200);
        mainContent.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                MembersActivity.this.onRefresh();
            }
        });
    }

    @Override
    public int getContentView() {
        return R.layout.activity_members;
    }

    @Override
    public AppCompatActivity getActivity() {
        return this;
    }

    @Override
    public int getActionTitle() {
        return R.string.members_title;
    }

    public void onRefresh() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mainContent.setRefreshing(false);
            }
        }, 1000);
    }
}
