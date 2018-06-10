package monsterstack.io.partner.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import butterknife.ButterKnife;
import monsterstack.io.api.listeners.OnResponseListener;
import monsterstack.io.api.resources.HttpError;
import monsterstack.io.partner.R;
import monsterstack.io.partner.adapter.control.GroupAdapterControl;
import monsterstack.io.partner.adapter.presenter.GroupAdapterPresenter;
import monsterstack.io.partner.domain.Group;
import monsterstack.io.partner.domain.Member;
import monsterstack.io.partner.main.MemberActivity;
import monsterstack.io.streamview.CardPagerAdapter;

public class GroupAdapter extends CardPagerAdapter<Group> implements GroupAdapterControl {
    private Context context;



    private GroupAdapterPresenter presenter;

    private ViewAnimatedListener viewAnimatedListener;

    public GroupAdapter(Context context, List<Group> dataList) {
        super(dataList);
        this.context = context;
    }

    public void setViewAnimatedListener(ViewAnimatedListener viewAnimated) {
        this.viewAnimatedListener = viewAnimated;
    }

    @Override
    public Context getContext() {
        return this.context;
    }

    @Override
    public ViewAnimatedListener getViewAnimatedListener() {
        return this.viewAnimatedListener;
    }

    @Override
    public void onMemberSelected(Member selectedMember) {
        Intent intent = new Intent(this.context, MemberActivity.class);
        intent.putExtra(MemberActivity.EXTRA_MEMBER, selectedMember);
        // Show Member Details Modal
        context.startActivity(intent);
        ((Activity)context).overridePendingTransition(R.anim.slide_up, R.anim.hold);
    }

    @Override
    public void bind(final Group data, final View cardView) {
        this.presenter = new GroupAdapterPresenter(this);
        ButterKnife.bind(presenter, cardView);

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("group", data);
        presenter.present(Optional.<Map>of(metadata));
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

    @Override
    public void findGroupMembers(Group group, final OnResponseListener<Member[], HttpError> onResponseListener) {

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
                members[7] = new Member("Eddy", "Longin", 8, Member.avatars[3]);
                members[8] = new Member("Carlos", "Manuel", 9, null);
                members[9] = new Member("Jay", "Darfler", 10, Member.avatars[4]);
                members[11] = new Member("Jeff", "Den", 12, Member.avatars[4]);

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
