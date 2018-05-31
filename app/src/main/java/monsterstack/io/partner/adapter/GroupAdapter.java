package monsterstack.io.partner.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import monsterstack.io.api.listeners.OnResponseListener;
import monsterstack.io.api.resources.HttpError;
import monsterstack.io.avatarview.AvatarView;
import monsterstack.io.avatarview.User;
import monsterstack.io.horizontallistview.HorizontalListView;
import monsterstack.io.partner.R;
import monsterstack.io.partner.domain.Group;
import monsterstack.io.partner.domain.Member;
import monsterstack.io.partner.main.MemberActivity;
import monsterstack.io.partner.utils.TypefaceUtils;
import monsterstack.io.streamview.CardPagerAdapter;

public class GroupAdapter extends CardPagerAdapter<Group> {
    private Context context;

    @BindView(R.id.raisedToDateValue)
    TextView raisedToDateValue;

    public GroupAdapter(Context context, List<Group> dataList) {
        super(dataList);
        this.context = context;
    }

    @Override
    public void bind(final Group data, final View view) {
        ButterKnife.bind(this, view);

        TypefaceUtils.useRobotoRegular(view.getContext(), raisedToDateValue);
        final HorizontalListView recyclerView = view.findViewById(R.id.membersView);

        findGroupMembers(data, new OnResponseListener<Member[], HttpError>() {
            @Override
            public void onResponse(final Member[] members, HttpError httpError) {
                final int index = (int)Math.floor(Math.random()*5);
                final Member onDeck = members[index];

                AvatarView onDeckAvatar = view.findViewById(R.id.onDeckAvatar);

                onDeckAvatar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) { ;
                        // Activity transition to Member Details
                        Intent intent = new Intent(context, MemberActivity.class);
                        intent.putExtra("member", onDeck);
                        Bundle options = ActivityOptionsCompat.makeScaleUpAnimation(
                                view, 0, 0, view.getWidth(), view.getHeight()).toBundle();

                        ActivityCompat.startActivity(context, intent, options);

                    }
                });

                TextView onDeckName = view.findViewById(R.id.onDeckName);
                TypefaceUtils.useRobotoRegular(view.getContext(), onDeckName);

                onDeckName.setText(onDeck.getFullName());
                onDeckAvatar.setUser(new User(onDeck.getFullName(),
                        onDeck.getAvatar(), R.color.colorAccent));

                MemberRecyclerViewAdapter adapter = new MemberRecyclerViewAdapter(view.getContext(), members);
                adapter.setClickListener(new MemberRecyclerViewAdapter.ItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Member clickedMember = members[position];
                        // Activity transition to Member Details
                        Intent intent = new Intent(context, MemberActivity.class);
                        intent.putExtra("member", clickedMember);
                        Bundle options = ActivityOptionsCompat.makeScaleUpAnimation(
                                view, 0, 0, view.getWidth(), view.getHeight()).toBundle();

                        ActivityCompat.startActivity(context, intent, options);
                    }
                });

                recyclerView.setAdapter(adapter);
            }
        });

    }

    @Override
    public int getCardViewId() {
        return R.id.cardView;
    }

    @Override
    public int getLayout() {
        return R.layout.group_adapter;
    }

    private void findGroupMembers(Group group, final OnResponseListener<Member[], HttpError> onResponseListener) {
        Member[] members = new Member[5];
        members[0] = new Member("Zach", "Rote", 1, Member.avatars[0]);
        members[1] = new Member("James", "Smith", 2, Member.avatars[1]);
        members[2] = new Member("Eddy", "Lee", 3, Member.avatars[3]);
        members[3] = new Member("Carl", "Morris", 4, Member.avatars[2]);
        members[4] = new Member("Jay", "Darfler", 5, Member.avatars[4]);

        // I/Choreographer: Skipped 79 frames!  The application may be doing too much work on its main thread.
        onResponseListener.onResponse(members, null);
    }
}
