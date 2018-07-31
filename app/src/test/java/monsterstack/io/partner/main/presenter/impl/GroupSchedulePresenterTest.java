package monsterstack.io.partner.main.presenter.impl;

import android.view.Menu;

import com.applandeo.materialcalendarview.CalendarView;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import monsterstack.io.partner.main.control.GroupScheduleControl;
import monsterstack.io.partner.main.presenter.GroupSchedulePresenter;

import static java.lang.Boolean.FALSE;
import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class GroupSchedulePresenterTest {
    @InjectMocks
    private GroupSchedulePresenterImpl groupSchedulePresenter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        // Prepare bindings
        groupSchedulePresenter.calendarView = mock(CalendarView.class);
    }

    @Test
    public void whenBinding_expectControllerToBeSet() {
        GroupScheduleControl mockControl = mock(GroupScheduleControl.class);
        groupSchedulePresenter.bind(mockControl);

        assertEquals(mockControl, groupSchedulePresenter.getControl());
    }

    @Test
    public void whenPresenting_expectPresenterToMatchCaller() {
        GroupSchedulePresenter presenter = (GroupSchedulePresenter)groupSchedulePresenter.present(Optional.empty());

        assertEquals(groupSchedulePresenter, presenter);
    }

    @Test
    public void onCreateMenuOptions_expectReturnFalse() {
        boolean result = groupSchedulePresenter.onCreateOptionsMenu(mock(Menu.class));

        assertEquals(FALSE, Boolean.valueOf(result));
    }
}
