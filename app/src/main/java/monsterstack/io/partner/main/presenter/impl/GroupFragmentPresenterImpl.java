package monsterstack.io.partner.main.presenter.impl;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.leinardi.android.speeddial.SpeedDialView;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

import butterknife.BindView;
import butterknife.OnClick;
import monsterstack.io.partner.R;
import monsterstack.io.partner.adapter.GroupAdapter;
import monsterstack.io.partner.common.presenter.Presenter;
import monsterstack.io.partner.domain.Group;
import monsterstack.io.partner.main.control.GroupFragmentControl;
import monsterstack.io.partner.main.presenter.GroupFragmentPresenter;
import monsterstack.io.streamview.PausingViewPager;
import monsterstack.io.streamview.ShadowTransformer;

import static android.support.v4.view.ViewPager.SCROLL_STATE_DRAGGING;
import static android.support.v4.view.ViewPager.SCROLL_STATE_SETTLING;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class GroupFragmentPresenterImpl implements GroupFragmentPresenter {
    @BindView(R.id.viewPager)
    PausingViewPager viewPager;

    @BindView(R.id.speedDial)
    SpeedDialView speedDialView;

    @BindView(R.id.progressbar)
    ProgressBar progressBar;

    @BindView(R.id.emptyState_get_started)
    LinearLayout getStartedCard;

    @BindView(R.id.create_group_button)
    Button createImageButton;

    GroupAdapter cardAdapter;

    private GroupFragmentControl control;
    private Group[] groupArray;

    public Presenter<GroupFragmentControl> present(Optional<Map> metadata) {
        showProgressBar();
        control.findGroupsAssociatedWithUser((groups, httpError) -> {
            if (null != httpError) {
                // Show Error here
                GroupFragmentPresenterImpl.this.groupArray = new Group[0];
                hideProgressBar();
            } else {

                if (null == groups)
                    groups = new Group[0];

                GroupFragmentPresenterImpl.this.groupArray = groups;

                if (groups.length == 0) {
                    getStartedCard.setVisibility(VISIBLE);
                    hideProgressBar();
                } else {
                    prepareCardAdapter(groups);
                    hideProgressBar();
                }
            }
        });
        return this;
    }

    @Override
    public SpeedDialView getSpeedDialView() {
        return speedDialView;
    }

    @Override
    public Context getContext() {
        return control.getContext();
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

    @Override
    public void pauseViewPager() {
        viewPager.setPaused(true);
    }

    @Override
    public void unpauseViewPager() {
        viewPager.setPaused(false);
    }

    @Override
    public void refreshGroups() {
        showProgressBar();
        control.findGroupsAssociatedWithUser((groups, httpError) -> {
            if (null != httpError) {
                // Show Error here
                showHttpError("Group Refresh", "Refresh Failed", httpError);
            } else {
                if (groups != null) {
                    if (null != this.cardAdapter) {
                        this.groupArray = groups;
                        this.cardAdapter.replaceGroups(groups);

                        int count = this.cardAdapter.getCount();
                        viewPager.setCurrentItem(count-1, true);
                    } else {
                        prepareCardAdapter(groups);
                    }
                }
            }

            viewPager.postDelayed(() -> hideProgressBar(), 5000);
        });
    }

    @Override
    public void prepareViewPager(final ViewPager viewPager, final GroupAdapter cardAdapter) {
        int margin = getCardMargin();
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

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(GONE);
    }

    @Override
    public void showProgressBar() {
        progressBar.setVisibility(VISIBLE);
    }

    @Override
    @OnClick(R.id.create_group_button)
    public void createGroup(View view) {
        control.createGroup();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public Presenter<GroupFragmentControl> bind(GroupFragmentControl control) {
        this.control = control;
        return this;
    }

    @Override
    public GroupFragmentControl getControl() {
        return control;
    }

    protected int getCardMargin() {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, -20,
                control.getContext().getResources().getDisplayMetrics());
    }

    protected void prepareCardAdapter(Group[] groups) {
        getStartedCard.setVisibility(GONE);

        this.speedDialView.setVisibility(VISIBLE);
        this.cardAdapter = new GroupAdapter(control.getContext(), Arrays.asList(groups));

        control.inflateFloatingActionButton(groups);

        control.listenForCardViewAnimationEvents(cardAdapter);

        prepareViewPager(viewPager, cardAdapter);

        control.listenForSpeedDialSelections(speedDialView);
    }
}
