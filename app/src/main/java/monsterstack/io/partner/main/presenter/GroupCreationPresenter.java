package monsterstack.io.partner.main.presenter;

import android.view.View;

import monsterstack.io.api.resources.Frequency;
import monsterstack.io.partner.common.HasDimmingSupport;
import monsterstack.io.partner.common.HasProgressBarSupport;
import monsterstack.io.partner.common.HasSnackBarSupport;
import monsterstack.io.partner.common.presenter.Presenter;
import monsterstack.io.partner.main.control.GroupCreationControl;

public interface GroupCreationPresenter extends Presenter<GroupCreationControl>, HasDimmingSupport,
        HasSnackBarSupport, HasProgressBarSupport {
    Integer getNumberOfSlots();
    Double getGoal();
    String getName();
    Frequency getFrequency();

    void disableBottomSheet();
    void enableBottomSheet();
    View getRootView();
}
