package monsterstack.io.partner.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import monsterstack.io.partner.R;
import monsterstack.io.partner.adapter.FriendArrayAdapter;
import monsterstack.io.partner.domain.Friend;
import monsterstack.io.partner.utils.NavigationUtils;

public class FriendsFragment extends Fragment {
    private View view;

    @BindView(R.id.friends_main_content)
    SwipeRefreshLayout mainContent;

    @BindView(R.id.friend_list)
    ListView listView;

    // Array of strings...
    private Friend[] friendArray = {
            new Friend("Zachary", "Rote", "zachary.rote@gmail.com", "+19544830245", Friend.avatars[0]),
            new Friend("Harrisen", "Rote", "harrisen.central@gmail.com", "+19544830245", Friend.avatars[1]),
            new Friend("Arabella", "Rote", "rote.arabella@gmail.com", "+19544830245", Friend.avatars[2])
    };

    public static FriendsFragment newInstance(String title) {
        Bundle args = new Bundle();
        args.putString("title", title);
        FriendsFragment instance = new FriendsFragment();

        instance.setArguments(args);

        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        this.view = inflater.inflate(
                R.layout.fragment_friends, container, false);

        ButterKnife.bind(this, this.view);

        ListView listView = this.view.findViewById(R.id.friend_list);
        listView.setAdapter(new FriendArrayAdapter(view.getContext(), friendArray ));


        mainContent.setProgressViewOffset(true, 100, 200);
        mainContent.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                FriendsFragment.this.onRefresh();
            }
        });

        return this.view;
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

    @OnClick(R.id.add_friends_button)
    public void onAddFriendsClicked() {
        Bundle bundle = NavigationUtils.enterStageBottom(getContext());
        Intent intent = new Intent(getContext(), AddFriendsActivity.class);

        startActivity(intent, bundle);
    }

}
