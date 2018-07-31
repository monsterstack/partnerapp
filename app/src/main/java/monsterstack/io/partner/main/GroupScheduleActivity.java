package monsterstack.io.partner.main;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.util.Optional;

import monsterstack.io.partner.Application;
import monsterstack.io.partner.R;
import monsterstack.io.partner.common.BasicActivity;

import monsterstack.io.partner.main.control.GroupScheduleControl;
import monsterstack.io.partner.main.presenter.GroupSchedulePresenter;

public class GroupScheduleActivity extends BasicActivity implements GroupScheduleControl {
    public static final String EXTRA_GROUP = "group";

    private GroupSchedulePresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = getPresenterFactory().getGroupSchedulePresenter(this, this);

        presenter.present(Optional.empty());
    }

    @Override
    public int getContentView() {
        return R.layout.group_schedule;
    }

    @Override
    public void injectDependencies(BasicActivity basicActivity) {
        super.injectDependencies(basicActivity);
        ((Application) getApplication()).component().inject(this);
    }

    @Override
    public AppCompatActivity getActivity() {
        return this;
    }

    @Override
    public int getActionTitle() {
        return R.string.group_schedule;
    }

    @Override
    public Context getContext() {
        return this;
    }
}
