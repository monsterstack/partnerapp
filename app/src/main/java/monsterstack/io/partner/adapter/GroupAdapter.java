package monsterstack.io.partner.adapter;

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
import monsterstack.io.partner.utils.TypefaceUtils;
import monsterstack.io.streamview.CardPagerAdapter;

public class GroupAdapter extends CardPagerAdapter<Group> {
    @BindView(R.id.raisedToDateValue)
    TextView raisedToDateValue;

    public GroupAdapter(List<Group> dataList) {
        super(dataList);
    }

    @Override
    public void bind(final Group data, final View view) {
        ButterKnife.bind(this, view);

        TypefaceUtils.useRobotoRegular(view.getContext(), raisedToDateValue);
        final HorizontalListView recyclerView = view.findViewById(R.id.membersView);

        findGroupMembers(data, new OnResponseListener<Member[], HttpError>() {
            @Override
            public void onResponse(Member[] members, HttpError httpError) {
                int index = (int)Math.floor(Math.random()*5);
                Member onDeck = members[index];

                AvatarView organizerAvatar = view.findViewById(R.id.onDeckAvatar);
                TextView organizerName = view.findViewById(R.id.onDeckName);
                TypefaceUtils.useRobotoRegular(view.getContext(), organizerName);

                organizerName.setText(onDeck.getFullName());
                organizerAvatar.setUser(new User(onDeck.getFullName(),
                        onDeck.getAvatar(), R.color.colorAccent));

                MemberRecyclerViewAdapter adapter = new MemberRecyclerViewAdapter(view.getContext(), members);
                adapter.setClickListener(new MemberRecyclerViewAdapter.ItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

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
