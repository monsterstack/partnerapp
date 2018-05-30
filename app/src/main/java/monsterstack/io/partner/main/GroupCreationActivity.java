package monsterstack.io.partner.main;

import android.icu.text.NumberFormat;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import monsterstack.io.partner.R;
import monsterstack.io.partner.common.BasicActivity;

public class GroupCreationActivity extends BasicActivity {
    @BindView(R.id.group_name)
    EditText groupNameInput;

    @BindView(R.id.group_numberOfSlots_display)
    TextView numberOfSlotsDisplay;

    @BindView(R.id.seekBar_numberOfSlots)
    SeekBar numberOfSlotsInput;

    @BindView(R.id.group_goal_display)
    TextView groupGoalDisplay;

    @BindView(R.id.seekBar_goal)
    SeekBar groupGoalInput;

    @BindView(R.id.group_calculated_duration)
    TextView calculatedDuration;

    @BindView(R.id.group_calculated_baseContribution)
    TextView calculatedBaseContribution;

    @BindView(R.id.group_contribution_freq_input)
    SwitchCompat contributionFrequencyInput;

    @BindView(R.id.group_contribution_freq_display)
    TextView contributionFrequencyDisplay;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);

        setUpForm();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.submit_action, menu);

        menu.findItem(R.id.submit_button).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                onSubmit(item);
                return false;
            }
        });
        return true;
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


    private void setUpForm() {

        Boolean weeklyContributionSelected = contributionFrequencyInput.isChecked();

        if (weeklyContributionSelected) {
            calculatedBaseContribution.setText(calculateBaseContributionInAmountPerWeek() + " / w");
            contributionFrequencyDisplay.setText("Weekly");
        } else {
            calculatedBaseContribution.setText(calculateBaseContributionInAmountPerMonth() + " / m");
            contributionFrequencyDisplay.setText("Monthly");
        }

        calculatedDuration.setText(calculateDurationInMonths() + " months");
        groupGoalDisplay.setText(NumberFormat.getCurrencyInstance().format(groupGoalInput.getProgress()));
        numberOfSlotsDisplay.setText(String.valueOf(numberOfSlotsInput.getProgress()));

        contributionFrequencyInput.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    calculatedBaseContribution.setText(calculateBaseContributionInAmountPerWeek() + " / w");
                    contributionFrequencyDisplay.setText("Weekly");
                } else {
                    calculatedBaseContribution.setText(calculateBaseContributionInAmountPerMonth() + " / m");
                    contributionFrequencyDisplay.setText("Monthly");
                }
            }
        });


        groupGoalInput.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                groupGoalDisplay.setText(NumberFormat.getCurrencyInstance().format(progress));
                Boolean weeklyContributionSelected = contributionFrequencyInput.isChecked();

                if (weeklyContributionSelected) {
                    calculatedBaseContribution.setText(calculateBaseContributionInAmountPerWeek() + " / w");
                } else {
                    calculatedBaseContribution.setText(calculateBaseContributionInAmountPerMonth() + " / m");
                }
                calculatedDuration.setText(calculateDurationInMonths() + " months");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        numberOfSlotsInput.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                numberOfSlotsDisplay.setText(String.valueOf(progress));
                Boolean weeklyContributionSelected = contributionFrequencyInput.isChecked();

                if (weeklyContributionSelected) {
                    calculatedBaseContribution.setText(calculateBaseContributionInAmountPerWeek() + " / w");
                } else {
                    calculatedBaseContribution.setText(calculateBaseContributionInAmountPerMonth() + " / m");
                }                calculatedDuration.setText(calculateDurationInMonths() + " months");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void onSubmit(MenuItem menuItem) {

    }

    private String calculateBaseContributionInAmountPerMonth() {
        int numberOfSlots = numberOfSlotsInput.getProgress();
        int goal = groupGoalInput.getProgress();

        return NumberFormat.getCurrencyInstance().format((double) (goal/numberOfSlots));
    }

    private String calculateBaseContributionInAmountPerWeek() {
        int numberOfSlots = numberOfSlotsInput.getProgress();
        int goal = groupGoalInput.getProgress();

        return NumberFormat.getCurrencyInstance().format(((double) (goal/numberOfSlots)/4));
    }

    private int calculateDurationInMonths() {
        return numberOfSlotsInput.getProgress();
    }
}
