package monsterstack.io.partner.main;

import android.content.Context;
import android.content.Intent;
import android.icu.text.NumberFormat;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import monsterstack.io.partner.Application;
import monsterstack.io.partner.R;
import monsterstack.io.partner.common.BasicActivity;
import monsterstack.io.partner.common.HasDimmingSupport;
import monsterstack.io.partner.domain.Contact;
import monsterstack.io.partner.main.control.GroupCreationControl;
import monsterstack.io.partner.main.presenter.GroupCreationPresenter;

public class GroupCreationActivity extends BasicActivity implements GroupCreationControl, HasDimmingSupport {
    private static final Integer CREATE_GROUP_RESULT_CODE = 101;
    public static final String EXTRA_INVITES = "invited.contacts";

    private GroupCreationPresenter presenter;
    private List<Contact> contactList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.presenter = getPresenterFactory().getGroupCreationPresenter(this, this);

        this.contactList = (List<Contact>)getIntent().getSerializableExtra(EXTRA_INVITES);

        Map<String, Object> metadata = new HashMap<>();
        metadata.put(EXTRA_INVITES, contactList);
        presenter.present(Optional.of(metadata));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.submit_action, menu);

        menu.findItem(R.id.submit_button).setOnMenuItemClickListener(item -> {
            onSubmit(item);
            return false;
        });
        return true;
    }

    @Override
    public void injectDependencies(BasicActivity basicActivity) {
        super.injectDependencies(basicActivity);
        ((Application) getApplication()).component().inject(this);
    }

    @Override
    public int getContentView() {
        return R.layout.group_new;
    }

    @Override
    public AppCompatActivity getActivity() {
        return this;
    }

    @Override
    public int getActionTitle() {
        return R.string.group_new;
    }


    @Override
    public void onSubmit(MenuItem menuItem) {
        Intent data = new Intent();
        data.putExtra("groupId", UUID.randomUUID());
        data.putExtra("members", (Serializable)this.contactList);
        setResult(CREATE_GROUP_RESULT_CODE, data);

        finish();
    }

    @Override
    public String calculateBaseContributionInAmountPerMonth() {
        int numberOfSlots = presenter.getNumberOfSlots();
        double goal = presenter.getGoal();

        return NumberFormat.getCurrencyInstance().format(goal/numberOfSlots);
    }

    @Override
    public String calculateBaseContributionInAmountPerWeek() {
        int numberOfSlots = presenter.getNumberOfSlots();
        double goal = presenter.getGoal();

        return NumberFormat.getCurrencyInstance().format(goal/numberOfSlots /4);
    }

    @Override
    public String calculateBaseContributionPercentageOfGoal() {
        int numberOfSlots = presenter.getNumberOfSlots();
        double goal = presenter.getGoal();
        return NumberFormat.getPercentInstance().format((goal/numberOfSlots)/goal);
    }

    @Override
    public int calculateDurationInMonths() {
        return presenter.getNumberOfSlots();
    }

    @Override
    public Context getContext() {
        return this;
    }
}
