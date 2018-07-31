package monsterstack.io.partner.main.presenter.impl;

import android.content.Context;
import android.view.Menu;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import butterknife.BindView;
import monsterstack.io.partner.R;
import monsterstack.io.partner.common.presenter.Presenter;
import monsterstack.io.partner.main.control.GroupScheduleControl;
import monsterstack.io.partner.main.presenter.GroupSchedulePresenter;

public class GroupSchedulePresenterImpl implements GroupSchedulePresenter {

    @BindView(R.id.calendarView)
    CalendarView calendarView;

    private Context context;

    private GroupScheduleControl control;

    @Override
    public Presenter<GroupScheduleControl> present(Optional<Map> metadata) {
        loadCalendar();
        return this;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public Presenter<GroupScheduleControl> bind(GroupScheduleControl control) {
        this.control = control;
        return this;
    }

    @Override
    public GroupScheduleControl getControl() {
        return control;
    }

    private void loadCalendar() {
        List<EventDay> events = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        events.add(new EventDay(calendar, R.drawable.day_item_background));

        calendarView.setEvents(events);

    }
}
