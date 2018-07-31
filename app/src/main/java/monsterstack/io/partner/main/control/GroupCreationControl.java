package monsterstack.io.partner.main.control;

import android.view.MenuItem;

import monsterstack.io.partner.common.Control;

public interface GroupCreationControl extends Control {

    String calculateBaseContributionInAmountPerMonth();

    String calculateBaseContributionInAmountPerWeek();

    String calculateBaseContributionPercentageOfGoal();

    Double calculateBaseContributionPerMonth();
    Double calculateBaseContributionPerWeek();

    int calculateDurationInMonths();

    void onSubmit(MenuItem menuItem);
}
