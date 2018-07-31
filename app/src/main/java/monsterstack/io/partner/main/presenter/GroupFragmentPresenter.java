package monsterstack.io.partner.main.presenter;

import android.support.v4.view.ViewPager;
import android.view.View;

import com.leinardi.android.speeddial.SpeedDialView;

import monsterstack.io.partner.adapter.GroupAdapter;
import monsterstack.io.partner.common.HasAlertSupport;
import monsterstack.io.partner.common.HasProgressBarSupport;
import monsterstack.io.partner.common.HasSpeedDialSupport;
import monsterstack.io.partner.common.HasViewPagerSupport;
import monsterstack.io.partner.common.presenter.Presenter;
import monsterstack.io.partner.main.control.GroupFragmentControl;

public interface GroupFragmentPresenter extends Presenter<GroupFragmentControl>,HasSpeedDialSupport, HasViewPagerSupport, HasProgressBarSupport, HasAlertSupport {
    void refreshGroups();
    void prepareViewPager(ViewPager viewPager, GroupAdapter cardAdapter);
    SpeedDialView getSpeedDialView();

    void pauseViewPager();
    void unpauseViewPager();

    void createGroup(View view);
}
