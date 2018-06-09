package monsterstack.io.partner.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.util.Map;
import java.util.Optional;

import butterknife.ButterKnife;
import monsterstack.io.partner.R;
import monsterstack.io.partner.common.BasicActivity;
import monsterstack.io.partner.main.presenter.GroupSchedulePresenter;

public class GroupScheduleActivity extends BasicActivity {
    public static final String EXTRA_GROUP = "group";

    private GroupSchedulePresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new GroupSchedulePresenter(this);
        ButterKnife.bind(presenter, this);

        presenter.present(Optional.<Map>empty());
    }

    @Override
    public int getContentView() {
        return R.layout.group_schedule;
    }

    @Override
    public AppCompatActivity getActivity() {
        return this;
    }

    @Override
    public int getActionTitle() {
        return R.string.group_schedule;
    }



}
