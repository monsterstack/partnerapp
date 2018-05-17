package monsterstack.io.partner.adapter;

import android.view.View;
import android.widget.TextView;

import java.util.List;

import monsterstack.io.partner.R;
import monsterstack.io.partner.domain.Group;
import monsterstack.io.streamview.CardPagerAdapter;

public class GroupAdapter extends CardPagerAdapter<Group> {
    public GroupAdapter(List<Group> dataList) {
        super(dataList);
    }

    @Override
    public void bind(Group data, View view) {
        TextView titleTextView = view.findViewById(R.id.groupName);
        titleTextView.setText(data.getName());
    }

    @Override
    public int getCardViewId() {
        return R.id.cardView;
    }

    @Override
    public int getLayout() {
        return R.layout.group_adapter;
    }
}
