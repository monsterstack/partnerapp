package monsterstack.io.partner.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.leinardi.android.speeddial.SpeedDialView;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import monsterstack.io.partner.R;
import monsterstack.io.partner.adapter.GroupAdapter;
import monsterstack.io.partner.domain.Group;
import monsterstack.io.streamview.ShadowTransformer;

public class GroupsFragment extends Fragment {
    private View view;

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @BindView(R.id.speedDial)
    SpeedDialView speedDialView;

    // Array of strings...
    private Group[] groupArray = {
            new Group("My First Group", 10),
            new Group("My Second Group", 5),
            new Group("My Third Group", 4),
            new Group("My Fourth Group", 34),
            new Group("My Fifth Group", 23),
            new Group("My Sixth Group", 21)
    };

    public static GroupsFragment newInstance(String title) {
        Bundle args = new Bundle();
        args.putString("title", title);
        GroupsFragment instance = new GroupsFragment();

        instance.setArguments(args);

        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        this.view = inflater.inflate(
                R.layout.fragment_groups, container, false);

        ButterKnife.bind(this, this.view);

        GroupAdapter cardAdapter = new GroupAdapter(Arrays.asList(groupArray));
        viewPager.setAdapter(cardAdapter);
        viewPager.setPageTransformer(false, new ShadowTransformer(viewPager, cardAdapter));
        viewPager.setOffscreenPageLimit(3);

        speedDialView.inflate(R.menu.group_view_speed_dial);

        return this.view;
    }
}
