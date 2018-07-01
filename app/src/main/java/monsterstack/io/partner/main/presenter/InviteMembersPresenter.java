package monsterstack.io.partner.main.presenter;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Map;
import java.util.Optional;

import butterknife.BindView;
import butterknife.OnClick;
import monsterstack.io.gridlistview.GridListView;
import monsterstack.io.partner.R;
import monsterstack.io.partner.adapter.ContactArrayAdapter;
import monsterstack.io.partner.adapter.EndlessScrollListener;
import monsterstack.io.partner.adapter.SelectedContactArrayAdapter;
import monsterstack.io.partner.common.Control;
import monsterstack.io.partner.common.HasDimmingSupport;
import monsterstack.io.partner.common.HasProgressBarSupport;
import monsterstack.io.partner.common.presenter.Presenter;
import monsterstack.io.partner.domain.Contact;
import monsterstack.io.partner.main.control.InviteMembersControl;

import static android.view.View.GONE;

public class InviteMembersPresenter extends EndlessScrollListener implements Presenter<InviteMembersControl>,
        HasProgressBarSupport, HasDimmingSupport {

    private static final Integer MAX_SELECTIONS_ALLOWED = 12;

    private InviteMembersControl control;

    @BindView(R.id.memberInviteView)
    RelativeLayout memberInviteView;

    @BindView(R.id.member_invite_bottom_sheet_header)
    RelativeLayout memberInviteBottomSheetHeader;

    @BindView(R.id.contactList)
    RecyclerView contactListView;

    @BindView(R.id.selectedContactList)
    GridListView selectedContactListView;

    @BindView(R.id.contactSearchText)
    SearchView searchView;

    @BindView(R.id.emptyState_label)
    TextView emptyStateLabel;

    @BindView(R.id.progressbar)
    ProgressBar progressBar;

    @BindView(R.id.member_invite_bottom_sheet)
    View bottomSheet;

    @BindView(R.id.selectedContactList_size)
    TextView selectedContactListSize;

    @BindView(R.id.member_invite_bottom_sheet_arrow)
    ImageButton potentialMemberBottomSheetLeftArrow;

    private String query;

    private Integer numberSelected = 0;

    private ContactArrayAdapter contactArrayAdapter;
    private SelectedContactArrayAdapter selectedContactArrayAdapter;

    private BottomSheetBehavior bottomSheetBehavior;

    public InviteMembersPresenter(Control control) {
        super(new LinearLayoutManager(control.getContext()));
        this.control = (InviteMembersControl)control;
    }

    public void addSelectedContact(Contact contact) {
        if (null == selectedContactArrayAdapter) {
            emptyStateLabel.setVisibility(GONE);
            selectedContactListView.setVisibility(View.VISIBLE);
            initializeSelectedContactsArrayAdapter(contact);
        } else {
            selectedContactArrayAdapter.appendContact(contact);
        }

        numberSelected = numberSelected + 1;

        if (numberSelected.equals(MAX_SELECTIONS_ALLOWED)) {
            // Disable checkboxes on all unchecked.
            disableEnableUnCheckedBoxes(false, contactListView);

            memberInviteView.postDelayed(() -> {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                Toast.makeText(control.getContext(),
                        "You have reached the invite limit of " + MAX_SELECTIONS_ALLOWED + ".",
                        Toast.LENGTH_LONG).show();
            }, 500);
        }
    }

    public void removeSelectedContact(Contact contact) {

        if (null == contact)
            return;

        numberSelected = numberSelected - 1;

        if (numberSelected < MAX_SELECTIONS_ALLOWED) {
            // Enable checkboxes on all unchecked
            disableEnableUnCheckedBoxes(true, contactListView);
        }

        selectedContactArrayAdapter.removeContact(contact);
        if (selectedContactArrayAdapter.getItemCount() == 0) {
            selectedContactListView.setVisibility(GONE);
            emptyStateLabel.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public Presenter<InviteMembersControl> present(Optional<Map> metadata) {
        searchView.setIconified(true);
        searchView.setSubmitButtonEnabled(true);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (null == query || query.isEmpty()) {
                    control.search((contacts, httpError) ->
                        InviteMembersPresenter.this.contactArrayAdapter.replaceContacts(contacts));
                } else {
                    control.search(query, (contacts, httpError) ->
                            InviteMembersPresenter.this.contactArrayAdapter.replaceContacts(contacts));
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                InviteMembersPresenter.this.query = query;

                return false;
            }
        });

        searchView.setOnCloseListener(() -> {
            this.query = null;
            control.search((contacts, httpError) ->
                    InviteMembersPresenter.this.contactArrayAdapter.replaceContacts(contacts));
            return false;
        });

        // init the bottom sheet behavior
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

        // change the state of the bottom sheet
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        // set the peek height
        bottomSheetBehavior.setPeekHeight(185);

        memberInviteView.getForeground().setAlpha(LIGHTER);

        // when bottom sheet is expanded - disable contact list
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (BottomSheetBehavior.STATE_EXPANDED == newState) {
                    dim(memberInviteView);
                } else if (BottomSheetBehavior.STATE_COLLAPSED == newState){
                    brighten(memberInviteView);

                    if (numberSelected.equals(MAX_SELECTIONS_ALLOWED)) {
                        disableEnableUnCheckedBoxes(false, contactListView);
                    }
                } else if (BottomSheetBehavior.STATE_DRAGGING == newState) {
                    dim(memberInviteView);
                    if (numberSelected.equals(MAX_SELECTIONS_ALLOWED)) {
                        disableEnableUnCheckedBoxes(false, contactListView);
                    }
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                animateBottomSheetArrows(slideOffset);
            }
        });

        memberInviteBottomSheetHeader.setOnClickListener(v -> {
            if (BottomSheetBehavior.STATE_EXPANDED == bottomSheetBehavior.getState()) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            } else if (BottomSheetBehavior.STATE_COLLAPSED == bottomSheetBehavior.getState()){
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        contactListView.addOnScrollListener(this);

        // Find contacts
        loadInitialContacts();

        return this;
    }

    @Override
    public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
        if (totalItemsCount >= 10)
            fetchContacts();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(GONE);
    }

    @Override
    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.member_invite_bottom_sheet)
    public void toggleBottomSheet() {
        int currentState = bottomSheetBehavior.getState();

        if (currentState == BottomSheetBehavior.STATE_COLLAPSED) {
            currentState = BottomSheetBehavior.STATE_EXPANDED;
            bottomSheetBehavior.setState(currentState);
        } else {
            currentState = BottomSheetBehavior.STATE_COLLAPSED;
            bottomSheetBehavior.setState(currentState);
        }
    }

    @Override
    public Presenter<InviteMembersControl> bind(InviteMembersControl control) {
        this.control = control;
        return this;
    }

    @Override
    public InviteMembersControl getControl() {
        return control;
    }

    private void disableEnableUnCheckedBoxes(Boolean enable, ViewGroup viewGroup) {
        int count = viewGroup.getChildCount();

        for(int i = 0; i < count; i++) {
            View child = viewGroup.getChildAt(i);

            if (child instanceof AppCompatCheckBox) {
                AppCompatCheckBox checkBox = (AppCompatCheckBox)child;
                if (!checkBox.isChecked())
                    checkBox.setEnabled(enable);
            } else if (child instanceof ViewGroup) {
                disableEnableUnCheckedBoxes(enable, (ViewGroup) child);
            }
        }
    }

    private void loadInitialContacts() {
        control.search((contacts, httpError) -> {
            InviteMembersPresenter.this.contactArrayAdapter = new ContactArrayAdapter(control.getContext(), contacts);

            InviteMembersPresenter.this.contactArrayAdapter.setOnContactSelectedHandler(contact -> {
                control.addSelectedContact(contact);
                return null;
            });

            InviteMembersPresenter.this.contactArrayAdapter.setOnContactUnSelectedHandler(contact -> {
                control.removeSelectedContact(contact);
                return null;
            });

            contactListView.setLayoutManager(getLayoutManager());
            contactListView.setAdapter(InviteMembersPresenter.this.contactArrayAdapter);

            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(control.getContext(),
                    DividerItemDecoration.VERTICAL);

            contactListView.addItemDecoration(dividerItemDecoration);
        });

    }

    private void initializeSelectedContactsArrayAdapter(Contact contact) {
        // Initialize Selected Contact List
        Contact[] contacts = new Contact[] {
                contact
        };
        this.selectedContactArrayAdapter = new SelectedContactArrayAdapter(control.getContext(), contacts);

        selectedContactArrayAdapter.setOnContactAdded(size -> {
            selectedContactListSize.setText(String.valueOf(size));
            return null;
        });

        selectedContactArrayAdapter.setOnContactRemoved(size -> {
            selectedContactListSize.setText(String.valueOf(size));
            return null;
        });

        selectedContactListView.setAdapter(this.selectedContactArrayAdapter);
        selectedContactListView.setLayoutManager(new GridLayoutManager(
                control.getContext(), 4, LinearLayoutManager.VERTICAL, false)
        );
    }


    private void fetchContacts() {
        showProgressBar();
        if (null == query || query.isEmpty()) {
            control.search((contacts, httpError) -> {
                InviteMembersPresenter.this.contactArrayAdapter.appendToContacts(contacts);
                hideProgressBar();
            });
        } else {
            control.search(query, (contacts, httpError) -> {
                InviteMembersPresenter.this.contactArrayAdapter.appendToContacts(contacts);
                hideProgressBar();
            });
        }
    }

    private void animateBottomSheetArrows(float slideOffset) {
        // Animate counter-clockwise
        potentialMemberBottomSheetLeftArrow.setRotation(slideOffset * 180);
    }
}
