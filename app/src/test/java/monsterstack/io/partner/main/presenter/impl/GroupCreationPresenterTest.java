package monsterstack.io.partner.main.presenter.impl;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.reflect.Whitebox;

import java.util.Optional;

import monsterstack.io.api.resources.Frequency;
import monsterstack.io.partner.common.Control;
import monsterstack.io.partner.domain.Contact;
import monsterstack.io.partner.main.control.GroupCreationControl;
import monsterstack.io.partner.main.presenter.GroupCreationPresenter;

import static java.lang.Boolean.FALSE;
import static junit.framework.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GroupCreationPresenterTest {
    @InjectMocks
    private GroupCreationPresenterImpl groupCreationPresenter;
    @Mock
    private GroupCreationControl mockControl;

    @Captor
    private ArgumentCaptor<TextWatcher> textWatcherArgumentCaptor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        // Prepare Bindings
        groupCreationPresenter.groupCreationView = mock(View.class);
        groupCreationPresenter.groupNameInput = mock(EditText.class);
        groupCreationPresenter.contributionFrequencyInput = mock(SwitchCompat.class);
        groupCreationPresenter.calculatedBaseContribution = mock(TextView.class);
        groupCreationPresenter.calculatedBaseContributionPercentage = mock(TextView.class);
        groupCreationPresenter.contributionFrequencyDisplay = mock(TextView.class);
        groupCreationPresenter.calculatedDuration = mock(TextView.class);
        groupCreationPresenter.groupGoalInput = mock(EditText.class);
        groupCreationPresenter.progressBar = mock(ProgressBar.class);
    }

    @Test
    public void whenBinding_expectControllerToBeSet() {
        GroupCreationControl mockControl = mock(GroupCreationControl.class);
        groupCreationPresenter.bind(mockControl);

        assertEquals(mockControl, groupCreationPresenter.getControl());
    }

    @Test
    public void whenPresenting_expectPresenterToMatchCaller() {
        GroupCreationPresenterImpl spiedOnPresenter = spy(groupCreationPresenter);
        doNothing().when(spiedOnPresenter).enableSoftInputMode();
        doNothing().when(spiedOnPresenter).prepareBaseContributionBottomSheet();
        doNothing().when(spiedOnPresenter).preparePotentialMembersBottomSheet(any(Contact[].class));

        GroupCreationPresenter presenter = (GroupCreationPresenter)spiedOnPresenter.present(Optional.empty());

        verify(spiedOnPresenter.groupGoalInput, times(1))
                .addTextChangedListener(textWatcherArgumentCaptor.capture());

        TextWatcher capturedWatcher = textWatcherArgumentCaptor.getValue();

        doReturn(true).when(spiedOnPresenter.contributionFrequencyInput).isChecked();

        capturedWatcher.onTextChanged(new String("500.00"), 1, 10, 0);

        verify(spiedOnPresenter.contributionFrequencyInput, atLeast(1))
                .isChecked();

        verify(mockControl, atLeast(1)).calculateBaseContributionInAmountPerWeek();
        verify(groupCreationPresenter.contributionFrequencyDisplay, times(1))
                .setText(same("Weekly"));

        doReturn(false).when(spiedOnPresenter.contributionFrequencyInput).isChecked();

        capturedWatcher.onTextChanged(new String("254.09"), 1, 10, 0);

        verify(spiedOnPresenter.contributionFrequencyInput, atLeast(1))
                .isChecked();

        verify(mockControl, atLeast(1)).calculateBaseContributionInAmountPerMonth();
        verify(groupCreationPresenter.contributionFrequencyDisplay, atLeast(1))
                .setText(same("Monthly"));

        assertEquals(spiedOnPresenter, presenter);
    }

    @Test
    public void onCreateMenuOptions_expectReturnFalse() {
        boolean result = groupCreationPresenter.onCreateOptionsMenu(mock(Menu.class));

        assertEquals(FALSE, Boolean.valueOf(result));
    }

    @Test
    public void givenWeeklyContributionSet_expectGetFrequencyReturnsWeekly() {
        when(groupCreationPresenter.contributionFrequencyInput.isChecked())
                .thenReturn(true);

        Frequency frequency = groupCreationPresenter.getFrequency();

        assertEquals(Frequency.weekly, frequency);
    }

    @Test
    public void whenHidingProgressBar_expectVisibilityGone() {
        groupCreationPresenter.hideProgressBar();

        verify(groupCreationPresenter.progressBar, times(1))
                .setVisibility(View.GONE);
    }

    @Test
    public void whenShowingProgressBar_expectVisibilityVisible() {
        groupCreationPresenter.showProgressBar();

        verify(groupCreationPresenter.progressBar, times(1))
                .setVisibility(View.VISIBLE);
    }

    @Test
    public void givenPotentialMembersIsEmpty_whenGettingNumberOfSlots_expectZero() {
        Whitebox.setInternalState(groupCreationPresenter, "potentialMembers", new Contact[0]);
        Integer numberOfSlots = groupCreationPresenter.getNumberOfSlots();

        assertEquals(Integer.valueOf(0), numberOfSlots);
    }

    @Test
    public void givenGroupNameIsFamily_whenGettingName_expectFamily() {
        Editable mockEditable = mock(Editable.class);
        when(mockEditable.toString()).thenReturn("Family");
        when(groupCreationPresenter.groupNameInput.getText()).thenReturn(mockEditable);

        String name = groupCreationPresenter.getName();

        assertEquals("Family", name);
    }

    @Test
    public void givenEmptyGoalText_whenGettingGoal_expectZero() {
        Editable mockEditable = mock(Editable.class);
        when(mockEditable.toString()).thenReturn("");
        when(groupCreationPresenter.groupGoalInput.getText()).thenReturn(mockEditable);
        Double goal = groupCreationPresenter.getGoal();

        assertEquals(0.0, goal);
    }

    @Test
    public void givenNullGoalText_whenGettingGoal_expectZero() {
        when(groupCreationPresenter.groupGoalInput.getText()).thenReturn(null);
        Double goal = groupCreationPresenter.getGoal();

        assertEquals(0.0, goal);
    }

    @Test
    public void givenFiveHundredGoalText_whenGettingGoal_expectFiveHundred() {
        Editable mockEditable = mock(Editable.class);
        when(mockEditable.toString()).thenReturn("500.00");
        when(groupCreationPresenter.groupGoalInput.getText()).thenReturn(mockEditable);
        Double goal = groupCreationPresenter.getGoal();

        assertEquals(500.0, goal);
    }


    public class TestActivity extends AppCompatActivity implements Control {

        @Override
        public Context getContext() {
            return this;
        }
    }
}
