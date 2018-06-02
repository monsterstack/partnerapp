package monsterstack.io.partner.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import monsterstack.io.api.listeners.OnResponseListener;
import monsterstack.io.api.resources.HttpError;
import monsterstack.io.gridlistview.GridListView;
import monsterstack.io.gridlistview.SpacesItemDecoration;
import monsterstack.io.partner.R;
import monsterstack.io.partner.anim.GroupCardJoinAnimator;
import monsterstack.io.partner.anim.GroupCardMemberAnimator;
import monsterstack.io.partner.domain.Group;
import monsterstack.io.partner.domain.GroupEntryOpportunity;
import monsterstack.io.partner.domain.Member;
import monsterstack.io.streamview.CardPagerAdapter;

import static android.view.View.VISIBLE;

public class GroupAdapter extends CardPagerAdapter<Group> {
    private Context context;
    private CardView cardView;

    @BindView(R.id.membersView)
    RecyclerView memberDetailsView;

    public GroupAdapter(Context context, List<Group> dataList) {
        super(dataList);
        this.context = context;
    }

    @Override
    public void bind(final Group data, final View cardView) {
        this.cardView = (CardView)cardView;
        ButterKnife.bind(this, cardView);

        final GridListView recyclerView = cardView.findViewById(R.id.membersView);
        recyclerView.addItemDecoration(new SpacesItemDecoration(context, R.dimen.member_grid_item_offset));
        findGroupMembers(data, new OnResponseListener<Member[], HttpError>() {
            @Override
            public void onResponse(final Member[] members, HttpError httpError) {
                final MemberRecyclerViewAdapter adapter = new MemberRecyclerViewAdapter(cardView.getContext(), members);
                adapter.setClickListener(new MemberRecyclerViewAdapter.ItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if(recyclerView.getVisibility() == VISIBLE) {
                            if (null != members[position]) {
                                Member clickedMember = members[position];
                                new GroupCardMemberAnimator((CardView) cardView).scaleUp(clickedMember);
                            } else {
                                new GroupCardJoinAnimator((CardView) cardView).animateIn(new GroupEntryOpportunity(data, position+1));
                            }
                        }
                    }
                });

                recyclerView.setAdapter(adapter);
            }
        });

    }

    public void reset() {
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

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                Member[] members = new Member[12];
                members[0] = new Member("Zach", "Rote", 1, Member.avatars[0]);
                members[1] = new Member("James", "Smith", 2, Member.avatars[1]);
                members[2] = new Member("Eddy", "Lee", 3, Member.avatars[3]);
                members[3] = new Member("Carl", "Morris", 4, Member.avatars[2]);
                members[4] = new Member("Jay", "Darfler", 5, Member.avatars[4]);
                members[5] = new Member("Zach", "Rote", 6, null);
//                members[6] = new Member("James", "Red", 7, Member.avatars[1]);
//                members[7] = new Member("Eddy", "Longin", 8, Member.avatars[3]);
//                members[8] = new Member("Carlos", "Manuel", 9, null);
//                members[9] = new Member("Jay", "Darfler", 10, Member.avatars[4]);
//                members[10] = new Member("Peter", "Mist", 11, null);
//                members[11] = new Member("Jeff", "Den", 12, Member.avatars[4]);

                final Member[] copy = members;
                ((Activity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onResponseListener.onResponse(copy, null);
                    }});
            }
        });
    }
}
