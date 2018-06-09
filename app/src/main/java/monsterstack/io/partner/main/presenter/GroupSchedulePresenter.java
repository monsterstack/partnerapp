package monsterstack.io.partner.main.presenter;

import android.content.Context;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import butterknife.BindView;
import monsterstack.io.partner.R;
import monsterstack.io.partner.common.presenter.PresenterAdapter;

public class GroupSchedulePresenter extends PresenterAdapter {

    @BindView(R.id.calendarView)
    CalendarView calendarView;

    private Context context;

    public GroupSchedulePresenter(Context context) {
        this.context = context;
    }

    @Override
    public void present(Optional<Map> metadata) {
        loadCalendar();
    }

    private void loadCalendar() {
        List<EventDay> events = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        events.add(new EventDay(calendar, R.drawable.day_item_background));

        calendarView.setEvents(events);

    }
}
