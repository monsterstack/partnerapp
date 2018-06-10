package monsterstack.io.partner.main.presenter;

import android.support.v4.view.ViewPager;
import android.util.TypedValue;

import com.leinardi.android.speeddial.SpeedDialView;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

import butterknife.BindView;
import monsterstack.io.api.listeners.OnResponseListener;
import monsterstack.io.api.resources.HttpError;
import monsterstack.io.partner.R;
import monsterstack.io.partner.adapter.GroupAdapter;
import monsterstack.io.partner.common.HasSpeedDialSupport;
import monsterstack.io.partner.common.HasViewPagerSupport;
import monsterstack.io.partner.common.presenter.PresenterAdapter;
import monsterstack.io.partner.domain.Group;
import monsterstack.io.partner.main.control.GroupFragmentControl;
import monsterstack.io.streamview.PausingViewPager;
import monsterstack.io.streamview.ShadowTransformer;

import static android.support.v4.view.ViewPager.SCROLL_STATE_DRAGGING;
import static android.support.v4.view.ViewPager.SCROLL_STATE_SETTLING;

public class GroupFragmentPresenter extends PresenterAdapter implements HasSpeedDialSupport, HasViewPagerSupport {
    @BindView(R.id.viewPager)
    PausingViewPager viewPager;

    @BindView(R.id.speedDial)
    SpeedDialView speedDialView;

    private GroupFragmentControl control;
    private Group[] groupArray;

    public GroupFragmentPresenter(GroupFragmentControl control) {
        this.control = control;
    }

    public void present(Optional<Map> metadata) {
        control.findGroupsAssociatedWithUser(new OnResponseListener<Group[], HttpError>() {
            @Override
            public void onResponse(Group[] groups, HttpError httpError) {
                GroupFragmentPresenter.this.groupArray = groups;
                final GroupAdapter cardAdapter = new GroupAdapter(control.getContext(), Arrays.asList(groups));

                control.inflateFloatingActionButton(groups);

                control.listenForCardViewAnimationEvents(cardAdapter);

                prepareViewPager(viewPager, cardAdapter);

                control.listenForSpeedDialSelections(speedDialView);

            }
        });
    }

    public SpeedDialView getSpeedDialView() {
        return speedDialView;
    }

    @Override
    public void inflateSpeedDial() {
        speedDialView.inflate(R.menu.group_view_speed_dial);
    }

    @Override
    public void hideSpeedDialView() {
        speedDialView.hide();
    }

    @Override
    public void showSpeedDialView() {
        speedDialView.show();
    }

    public void pauseViewPager() {
        viewPager.setPaused(true);
    }

    public void unpauseViewPager() {
        viewPager.setPaused(false);
    }

    public void prepareViewPager(final ViewPager viewPager, final GroupAdapter cardAdapter) {
        int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, -20,
                control.getContext().getResources().getDisplayMetrics());
        viewPager.setPageMargin(-margin);

        viewPager.setPageMargin(margin);
        viewPager.setAdapter(cardAdapter);
        viewPager.setPageTransformer(false, new ShadowTransformer(viewPager, cardAdapter));
        viewPager.setOffscreenPageLimit(5);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                control.setSelectedGroup(groupArray[position]);
                cardAdapter.reset();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == SCROLL_STATE_DRAGGING)
                    speedDialView.hide();
                else if (state == SCROLL_STATE_SETTLING)
                    speedDialView.show();
                else
                    speedDialView.show();
            }
        });
    }
}
