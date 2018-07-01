package monsterstack.io.partner.main.presenter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import butterknife.BindView;
import monsterstack.io.gridlistview.GridListView;
import monsterstack.io.partner.R;
import monsterstack.io.partner.adapter.SelectedContactArrayAdapter;
import monsterstack.io.partner.common.HasDimmingSupport;
import monsterstack.io.partner.common.presenter.Presenter;
import monsterstack.io.partner.domain.Contact;
import monsterstack.io.partner.main.control.GroupCreationControl;

public class GroupCreationPresenter implements Presenter<GroupCreationControl>, HasDimmingSupport {
    private static final String DEFAULT_GOAL = "150.00";

    @BindView(R.id.group_name)
    EditText groupNameInput;

    @BindView(R.id.group_numberOfSlots_display)
    TextView numberOfSlotsDisplay;

    @BindView(R.id.groupGoalInput)
    EditText groupGoalInput;

    @BindView(R.id.group_calculated_duration)
    TextView calculatedDuration;

    @BindView(R.id.group_calculated_baseContribution)
    TextView calculatedBaseContribution;

    @BindView(R.id.group_calculated_baseContributionPercentage)
    TextView calculatedBaseContributionPercentage;

    @BindView(R.id.group_contribution_freq_input)
    SwitchCompat contributionFrequencyInput;

    @BindView(R.id.group_contribution_freq_display)
    TextView contributionFrequencyDisplay;

    @BindView(R.id.groupCreationView)
    View groupCreationView;

    @BindView(R.id.emptyState_label)
    TextView emptyStateLabel;

    @BindView(R.id.selectedContactList_size)
    TextView potentialMemberCount;

    @BindView(R.id.member_invite_bottom_sheet_header)
    RelativeLayout memberInviteBottomSheetHeader;

    @BindView(R.id.baseContributionHeader)
    RelativeLayout baseContributionBottomSheetHeader;

    @BindView(R.id.member_invite_bottom_sheet)
    View potentialMembersBottomSheet;

    @BindView(R.id.member_base_contribution_bottom_sheet)
    View baseContributionBottomSheet;

    @BindView(R.id.selectedContactList)
    GridListView potentialMembersList;

    @BindView(R.id.member_invite_bottom_sheet_arrow)
    ImageButton potentialMemberBottomSheetLeftArrow;

    @BindView(R.id.base_contribution_bottom_sheet_arrow)
    ImageButton baseContributionBottomSheetLeftArrow;

    private BottomSheetBehavior potentialMembersBottomSheetBehavior;
    private BottomSheetBehavior baseContributionBottomSheetBehavior;

    private GroupCreationControl control;
    private Contact[] potentialMembers;

    @Override
    public Presenter<GroupCreationControl> present(Optional<Map> metadata) {
        if (metadata.isPresent()) {
            List<Contact> contactList = (List<Contact>)metadata.get().get("invited.contacts");
            setUpForm();
            prepareBaseContributionBottomSheet();
            preparePotentialMembersBottomSheet(contactList.toArray(new Contact[contactList.size()]));
        } else {
            setUpForm();
            prepareBaseContributionBottomSheet();
            preparePotentialMembersBottomSheet(new Contact[0]);
        }

        return this;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    public Integer getNumberOfSlots() {
        return null == potentialMembers ? 0 : potentialMembers.length;
    }

    public Double getGoal() {
        return groupGoalInput.getText() == null || groupGoalInput.getText().toString().isEmpty() ? 0.0
                : Double.valueOf(groupGoalInput.getText().toString());
    }

    @Override
    public Presenter<GroupCreationControl> bind(GroupCreationControl control) {
        this.control = control;

        return this;
    }

    @Override
    public GroupCreationControl getControl() {
        return control;
    }

    // -- Private
    private void setUpForm() {
        ((Activity)control).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        groupCreationView.setOnClickListener(view -> {
            InputMethodManager imm = (InputMethodManager) control.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
            //Find the currently focused view, so we can grab the correct window token from it.
            View currentFocus = ((Activity)control.getContext()).getCurrentFocus();
            //If no view currently has focus, create a new one, just so we can grab a window token from it
            if (view == null) {
                view = new View(control.getContext());
            }
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        });

        Boolean weeklyContributionSelected = contributionFrequencyInput.isChecked();

        if (weeklyContributionSelected) {
            calculatedBaseContribution.setText(control.calculateBaseContributionInAmountPerWeek() + " / w");
            calculatedBaseContributionPercentage.setText(control.calculateBaseContributionPercentageOfGoal());
            contributionFrequencyDisplay.setText("Weekly");
        } else {
            calculatedBaseContribution.setText(control.calculateBaseContributionInAmountPerMonth() + " / m");
            calculatedBaseContributionPercentage.setText(control.calculateBaseContributionPercentageOfGoal());
            contributionFrequencyDisplay.setText("Monthly");
        }

        calculatedDuration.setText(control.calculateDurationInMonths() + " months");

        contributionFrequencyInput.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                calculatedBaseContribution.setText(control.calculateBaseContributionInAmountPerWeek() + " / w");
                calculatedBaseContributionPercentage.setText(control.calculateBaseContributionPercentageOfGoal());
                contributionFrequencyDisplay.setText("Weekly");
            } else {
                calculatedBaseContribution.setText(control.calculateBaseContributionInAmountPerMonth() + " / m");
                calculatedBaseContributionPercentage.setText(control.calculateBaseContributionPercentageOfGoal());
                contributionFrequencyDisplay.setText("Monthly");
            }
        });

        groupGoalInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    Boolean isChecked = contributionFrequencyInput.isChecked();
                    if (isChecked) {
                        calculatedBaseContribution.setText(control.calculateBaseContributionInAmountPerWeek() + " / w");
                        calculatedBaseContributionPercentage.setText(control.calculateBaseContributionPercentageOfGoal());
                        contributionFrequencyDisplay.setText("Weekly");
                    } else {
                        calculatedBaseContribution.setText(control.calculateBaseContributionInAmountPerMonth() + " / m");
                        calculatedBaseContributionPercentage.setText(control.calculateBaseContributionPercentageOfGoal());
                        contributionFrequencyDisplay.setText("Monthly");
                    }
                } catch (Exception e) {
                    ; // eat it.
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void preparePotentialMembersBottomSheet(Contact[] potentialMembers) {
        this.potentialMembers = potentialMembers;
        // Initialize potential member list
        if (potentialMembers.length != 0) {
            emptyStateLabel.setVisibility(View.GONE);
        }

        groupGoalInput.setText(DEFAULT_GOAL);
        calculatedDuration.setText(control.calculateDurationInMonths() + " months");

        numberOfSlotsDisplay.setText(String.valueOf(potentialMembers.length));
        potentialMemberCount.setText(String.valueOf(potentialMembers.length));

        SelectedContactArrayAdapter selectedContactArrayAdapter =
                new SelectedContactArrayAdapter(control.getContext(), potentialMembers);
        potentialMembersList.setAdapter(selectedContactArrayAdapter);

        // init the bottom sheet behavior
        potentialMembersBottomSheetBehavior = BottomSheetBehavior.from(potentialMembersBottomSheet);

        // change the state of the bottom sheet
        potentialMembersBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        // set the peek height
        potentialMembersBottomSheetBehavior.setPeekHeight(195);

        groupCreationView.getForeground().setAlpha(LIGHTER);

        potentialMembersBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (BottomSheetBehavior.STATE_DRAGGING == newState ||
                        BottomSheetBehavior.STATE_EXPANDED == newState) {
                    baseContributionBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    dim((ViewGroup)groupCreationView);
                } else if (BottomSheetBehavior.STATE_COLLAPSED == newState){
                    brighten((ViewGroup)groupCreationView);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                animatePotentialMembersBottomSheetArrows(slideOffset);
            }
        });

        memberInviteBottomSheetHeader.setOnClickListener(v -> {
            if (potentialMembersBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                potentialMembersBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                dim((ViewGroup)groupCreationView);
            } else if (potentialMembersBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                potentialMembersBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                brighten((ViewGroup)groupCreationView);
            }
        });
    }

    private void prepareBaseContributionBottomSheet() {
        // init the bottom sheet behavior
        baseContributionBottomSheetBehavior = BottomSheetBehavior.from(baseContributionBottomSheet);

        // change the state of the bottom sheet
        baseContributionBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        // set the peek height
        baseContributionBottomSheetBehavior.setPeekHeight(480);


        groupCreationView.getForeground().setAlpha(LIGHTER);

        baseContributionBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (BottomSheetBehavior.STATE_DRAGGING == newState ||
                        BottomSheetBehavior.STATE_EXPANDED == newState) {
                    potentialMembersBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    dim((ViewGroup)groupCreationView);
                } else if (BottomSheetBehavior.STATE_COLLAPSED == newState){
                    brighten((ViewGroup)groupCreationView);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                animateBaseContributionsBottomSheetArrows(slideOffset);
            }
        });

        baseContributionBottomSheetHeader.setOnClickListener(v -> {
            if (baseContributionBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                baseContributionBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                dim((ViewGroup)groupCreationView);
            } else if (baseContributionBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                baseContributionBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                brighten((ViewGroup)groupCreationView);
            }
        });
    }

    private void animatePotentialMembersBottomSheetArrows(float slideOffset) {
        // Animate counter-clockwise
        potentialMemberBottomSheetLeftArrow.setRotation(slideOffset * 180);
    }

    private void animateBaseContributionsBottomSheetArrows(float slideOffset) {
        // Animate counter-clockwise
        baseContributionBottomSheetLeftArrow.setRotation(slideOffset * 180);
    }
}
