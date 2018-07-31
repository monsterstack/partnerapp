package monsterstack.io.partner.main.presenter.impl;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.reflect.Whitebox;

import java.util.Optional;

import monsterstack.io.api.listeners.OnResponseListener;
import monsterstack.io.api.resources.HttpError;
import monsterstack.io.partner.adapter.ContactArrayAdapter;
import monsterstack.io.partner.domain.Contact;
import monsterstack.io.partner.main.control.InviteMembersControl;
import monsterstack.io.partner.main.presenter.InviteMembersPresenter;

import static monsterstack.io.partner.common.HasDimmingSupport.LIGHTER;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class InviteMembersPresenterTest {
    @InjectMocks
    private InviteMembersPresenterImpl inviteMembersPresenter;

    @Mock
    private InviteMembersControl mockControl;

    @Captor
    private ArgumentCaptor<Integer> progressBarVisibilityCaptor;

    private Drawable mockFullViewForeground;
    private ContactArrayAdapter mockContactArrayAdapter;

    private CoordinatorLayout.LayoutParams mockCoordinatorLayoutParams;
    private Context mockContext;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        mockCoordinatorLayoutParams = mock(CoordinatorLayout.LayoutParams.class);
        mockFullViewForeground = mock(Drawable.class);
        mockContactArrayAdapter = mock(ContactArrayAdapter.class);
        mockContext = mock(Context.class);

        when(mockControl.getContext()).thenReturn(mockContext);
        when(mockCoordinatorLayoutParams.getBehavior()).thenReturn(mock(BottomSheetBehavior.class));

        // Prepare adapter
        Whitebox.setInternalState(inviteMembersPresenter, "contactArrayAdapter", mockContactArrayAdapter);

        // Prepare bindings
        inviteMembersPresenter.bottomSheet = mock(View.class);
        inviteMembersPresenter.progressBar = mock(ProgressBar.class);
        inviteMembersPresenter.contactListView = mock(RecyclerView.class);
        inviteMembersPresenter.memberInviteView = mock(RelativeLayout.class);
        inviteMembersPresenter.memberInviteBottomSheetHeader = mock(RelativeLayout.class);
        inviteMembersPresenter.searchView = mock(SearchView.class);
        inviteMembersPresenter.potentialMemberBottomSheetLeftArrow = mock(ImageButton.class);
        inviteMembersPresenter.bottomSheetBehavior = mock(BottomSheetBehavior.class);

        when(inviteMembersPresenter.bottomSheet.getLayoutParams()).thenReturn(mockCoordinatorLayoutParams);
        when(inviteMembersPresenter.memberInviteView.getForeground()).thenReturn(mockFullViewForeground);
    }

    @Test
    public void whenBindingControl_expectMatch() {
        InviteMembersPresenter presenter = (InviteMembersPresenter)inviteMembersPresenter.bind(mockControl);

        assertEquals(inviteMembersPresenter, presenter);
        assertEquals(mockControl, inviteMembersPresenter.getControl());
    }

    @Test
    public void whenPresenting_expectPresenterToMatchCaller() {
        InviteMembersPresenter presenter = (InviteMembersPresenter)inviteMembersPresenter.present(Optional.empty());
        assertEquals(inviteMembersPresenter, presenter);
    }

    @Test
    public void whenPresenting_expectViewToMeetExpectations() {
        // Arrange
        int startPage = 0;

        ArgumentCaptor<BottomSheetBehavior.BottomSheetCallback> bottomSheetCallbackArgumentCaptor = ArgumentCaptor.forClass(BottomSheetBehavior.BottomSheetCallback.class);

        InviteMembersPresenterImpl spiedOnPresenter = spy(inviteMembersPresenter);
        doReturn(mock(DividerItemDecoration.class)).when(spiedOnPresenter).createDividerItemDecoration();

        // Act
        InviteMembersPresenter presenter = (InviteMembersPresenter)spiedOnPresenter.present(Optional.empty());

        // Assert
        verify(spiedOnPresenter.bottomSheetBehavior, times(1)).setPeekHeight(185);
        verify(mockFullViewForeground, times(1)).setAlpha(LIGHTER);

        verify(spiedOnPresenter.bottomSheetBehavior, times(1)).setBottomSheetCallback(bottomSheetCallbackArgumentCaptor.capture());

        BottomSheetBehavior.BottomSheetCallback capturedCallback = bottomSheetCallbackArgumentCaptor.getValue();

        capturedCallback.onStateChanged(spiedOnPresenter.bottomSheet, BottomSheetBehavior.STATE_EXPANDED);

        verify(spiedOnPresenter, times(1)).onMemberInviteBottomSheetStateChanged(BottomSheetBehavior.STATE_EXPANDED);

        capturedCallback.onSlide(spiedOnPresenter.bottomSheet, 100f);

        verify(spiedOnPresenter, times(1)).onMemberInviteBottomSheetSlide(100f);

        verify(mockControl, times(1)).search(same(startPage), any(OnResponseListener.class));
        assertEquals(spiedOnPresenter, presenter);
    }

    @Test
    public void whenHidingProgressBar_expectVisibilityGone() {
        inviteMembersPresenter.hideProgressBar();

        verify(inviteMembersPresenter.progressBar, times(1)).setVisibility(
                progressBarVisibilityCaptor.capture());

        Integer capturedVisibility = progressBarVisibilityCaptor.getValue();

        assertEquals(View.GONE, capturedVisibility.intValue());
    }

    @Test
    public void whenShowingProgressBar_expectVisibilityVisible() {
        inviteMembersPresenter.showProgressBar();

        verify(inviteMembersPresenter.progressBar, times(1)).setVisibility(
                progressBarVisibilityCaptor.capture());

        Integer capturedVisibility = progressBarVisibilityCaptor.getValue();

        assertEquals(View.VISIBLE, capturedVisibility.intValue());
    }

    @Test
    public void givenBottomSheetIsCollapsed_whenTogglingBottomSheet_expectToBeExpanded() {
        when(inviteMembersPresenter.bottomSheetBehavior.getState()).thenReturn(BottomSheetBehavior.STATE_COLLAPSED);

        inviteMembersPresenter.toggleBottomSheet();

        verify(inviteMembersPresenter.bottomSheetBehavior, times(1)).setState(same(BottomSheetBehavior.STATE_EXPANDED));

        reset(inviteMembersPresenter.bottomSheetBehavior);
    }

    @Test
    public void givenBottomSheetIsExpanded_whenTogglingBottomSheet_expectToBeCollapsed() {
        when(inviteMembersPresenter.bottomSheetBehavior.getState()).thenReturn(BottomSheetBehavior.STATE_EXPANDED);

        inviteMembersPresenter.toggleBottomSheet();

        verify(inviteMembersPresenter.bottomSheetBehavior, times(1)).setState(same(BottomSheetBehavior.STATE_COLLAPSED));

        reset(inviteMembersPresenter.bottomSheetBehavior);
    }

    @Test
    public void givenTotalContactsGreaterThanTen_whenOnLoadMore_expectFetchContactsNoQueryCalled() {
        Integer startPage = 0;

        InviteMembersPresenterImpl spiedOnPresenter = spy(inviteMembersPresenter);
        spiedOnPresenter.onLoadMore(startPage, 10, mock(RecyclerView.class));

        ArgumentCaptor<Integer> pageCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<OnResponseListener<Contact[], HttpError>> onResponseListenerArgumentCaptor
                = ArgumentCaptor.forClass(OnResponseListener.class);
        verify(spiedOnPresenter, times(1)).fetchContacts(pageCaptor.capture());
        verify(mockControl, times(1)).search(same(startPage),
                onResponseListenerArgumentCaptor.capture());

        Integer capturedPage = pageCaptor.getValue();
        OnResponseListener<Contact[], HttpError> capturedListener = onResponseListenerArgumentCaptor.getValue();
        capturedListener.onResponse(contacts(), null);

        verify(mockContactArrayAdapter, times(1)).appendToContacts(any(Contact[].class));

        assertEquals(startPage.intValue(), capturedPage.intValue());

    }

    @Test
    public void givenTotalContactsGreaterThanTen_whenOnLoadMore_expectFetchContactsWithQueryCalled() {
        Integer startPage = 0;

        InviteMembersPresenterImpl spiedOnPresenter = spy(inviteMembersPresenter);
        Whitebox.setInternalState(spiedOnPresenter, "query", "Fred");

        spiedOnPresenter.onLoadMore(startPage, 10, mock(RecyclerView.class));

        ArgumentCaptor<Integer> pageCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<OnResponseListener<Contact[], HttpError>> onResponseListenerArgumentCaptor
                = ArgumentCaptor.forClass(OnResponseListener.class);
        verify(spiedOnPresenter, times(1)).fetchContacts(pageCaptor.capture());
        verify(mockControl, times(1)).search(same(startPage),
                same("Fred"),
                onResponseListenerArgumentCaptor.capture());


        Integer capturedPage = pageCaptor.getValue();
        OnResponseListener<Contact[], HttpError> capturedListener = onResponseListenerArgumentCaptor.getValue();
        capturedListener.onResponse(contacts(), null);
        verify(mockContactArrayAdapter, times(1)).appendToContacts(any(Contact[].class));


        assertEquals(startPage.intValue(), capturedPage.intValue());
    }

    @Test
    public void givenBottomSheetCollapsing_expectBrightenOnCollapse() {
        InviteMembersPresenterImpl spiedOnPresenter = spy(inviteMembersPresenter);

        spiedOnPresenter.onMemberInviteBottomSheetStateChanged(BottomSheetBehavior.STATE_COLLAPSED);

        verify(spiedOnPresenter, times(1)).brighten(any(ViewGroup.class));
    }

    @Test
    public void givenBottomSheetExpanding_expectDimOnCollapse() {
        InviteMembersPresenterImpl spiedOnPresenter = spy(inviteMembersPresenter);

        spiedOnPresenter.onMemberInviteBottomSheetStateChanged(BottomSheetBehavior.STATE_EXPANDED);

        verify(spiedOnPresenter, times(1)).dim(any(ViewGroup.class));
    }

    @Test
    public void givenBottomSheetDragging_expectDimOnDragging() {
        InviteMembersPresenterImpl spiedOnPresenter = spy(inviteMembersPresenter);

        spiedOnPresenter.onMemberInviteBottomSheetStateChanged(BottomSheetBehavior.STATE_DRAGGING);

        verify(spiedOnPresenter, times(1)).dim(any(ViewGroup.class));

    }

    @Test
    public void givenBottomSheetCollapsingAndMaxSelectionsMade_expectDisableControls() {
        InviteMembersPresenterImpl spiedOnPresenter = spy(inviteMembersPresenter);
        Whitebox.setInternalState(spiedOnPresenter, "numberSelected", InviteMembersPresenter.MAX_SELECTIONS_ALLOWED);

        spiedOnPresenter.onMemberInviteBottomSheetStateChanged(BottomSheetBehavior.STATE_COLLAPSED);

        ArgumentCaptor<Boolean> enableControlsCaptor = ArgumentCaptor.forClass(Boolean.class);

        verify(spiedOnPresenter, times(1))
                .disableEnableUnCheckedBoxes(enableControlsCaptor.capture(), any(ViewGroup.class));

        Boolean capturedEnabled = enableControlsCaptor.getValue();

        assertEquals(false, capturedEnabled.booleanValue());
    }

    @Test
    public void givenBottomSheetDraggingAndMaxSelectionsMade_expectDisableControls() {
        InviteMembersPresenterImpl spiedOnPresenter = spy(inviteMembersPresenter);
        Whitebox.setInternalState(spiedOnPresenter, "numberSelected", InviteMembersPresenter.MAX_SELECTIONS_ALLOWED);

        spiedOnPresenter.onMemberInviteBottomSheetStateChanged(BottomSheetBehavior.STATE_DRAGGING);

        ArgumentCaptor<Boolean> enableControlsCaptor = ArgumentCaptor.forClass(Boolean.class);

        verify(spiedOnPresenter, times(1))
                .disableEnableUnCheckedBoxes(enableControlsCaptor.capture(), any(ViewGroup.class));

        Boolean capturedEnabled = enableControlsCaptor.getValue();

        assertEquals(false, capturedEnabled.booleanValue());
    }

    @Test
    public void givenBottomSheetCollapsingAndNotMaxSelectionsMade_expectNotDisableControls() {
        InviteMembersPresenterImpl spiedOnPresenter = spy(inviteMembersPresenter);
        Whitebox.setInternalState(spiedOnPresenter, "numberSelected", 1);

        spiedOnPresenter.onMemberInviteBottomSheetStateChanged(BottomSheetBehavior.STATE_COLLAPSED);

        verify(spiedOnPresenter, times(0))
                .disableEnableUnCheckedBoxes(anyBoolean(), any(ViewGroup.class));
    }

    @Test
    public void givenBottomSheetExpanded_onMemberInviteBottomSheetHeaderClick_expectBottomSheetCollapsed() {
        when(inviteMembersPresenter.bottomSheetBehavior.getState()).thenReturn(BottomSheetBehavior.STATE_EXPANDED);
        inviteMembersPresenter.onMemberInviteBottomSheetHeaderClicked();

        verify(inviteMembersPresenter.bottomSheetBehavior, times(1)).setState(same(BottomSheetBehavior.STATE_COLLAPSED));

        reset(inviteMembersPresenter.bottomSheetBehavior);
    }

    @Test
    public void givenBottomSheetCollapsed_onMemberInviteBottomSheetHeaderClick_expectBottomSheetExpanded() {
        when(inviteMembersPresenter.bottomSheetBehavior.getState()).thenReturn(BottomSheetBehavior.STATE_COLLAPSED);
        inviteMembersPresenter.onMemberInviteBottomSheetHeaderClicked();

        verify(inviteMembersPresenter.bottomSheetBehavior, times(1)).setState(same(BottomSheetBehavior.STATE_EXPANDED));

        reset(inviteMembersPresenter.bottomSheetBehavior);
    }

    @Test
    public void givenQuerySet_onQueryTextSubmit_expectSearchWithQuery() {
        Contact[] contacts = contacts();
        ArgumentCaptor<OnResponseListener<Contact[], HttpError>> onResponseListenerArgumentCaptor = ArgumentCaptor.forClass(OnResponseListener.class);
        inviteMembersPresenter.onQueryTextSubmit("Fred");

        verify(mockControl, times(1)).search(same(0), same("Fred"), onResponseListenerArgumentCaptor.capture());

        OnResponseListener<Contact[], HttpError> capturedListener = onResponseListenerArgumentCaptor.getValue();

        capturedListener.onResponse(contacts, null);

        verify(mockContactArrayAdapter, times(1)).replaceContacts(same(contacts));

    }

    @Test
    public void givenNoQuerySet_onQueryTextSubmit_expectSearchWithoutQuery() {
        Contact[] contacts = contacts();
        ArgumentCaptor<OnResponseListener<Contact[], HttpError>> onResponseListenerArgumentCaptor = ArgumentCaptor.forClass(OnResponseListener.class);
        inviteMembersPresenter.onQueryTextSubmit(""); // empty string

        verify(mockControl, times(1)).search(same(0), onResponseListenerArgumentCaptor.capture());

        OnResponseListener<Contact[], HttpError> capturedListener = onResponseListenerArgumentCaptor.getValue();

        capturedListener.onResponse(contacts, null);

        verify(mockContactArrayAdapter, times(1)).replaceContacts(same(contacts));

    }

    @Test
    public void givenQuery_onQueryTextChange_expectQueryEqualsChangeValue() {
        boolean result = inviteMembersPresenter.onQueryTextChange("fred");

        assertEquals(false, result);
        assertEquals("fred", Whitebox.getInternalState(inviteMembersPresenter, "query").toString());
    }

    @Test
    public void onQueryClose_expectReset() {
        Contact[] contacts = contacts();
        ArgumentCaptor<OnResponseListener<Contact[], HttpError>> onResponseListenerArgumentCaptor =
                ArgumentCaptor.forClass(OnResponseListener.class);
        boolean result = inviteMembersPresenter.onClose();

        verify(mockControl, times(1)).resetPage();
        verify(mockControl, times(1)).search(same(0), onResponseListenerArgumentCaptor.capture());

        OnResponseListener<Contact[], HttpError> capturedListener = onResponseListenerArgumentCaptor.getValue();
        capturedListener.onResponse(contacts, null);

        verify(mockContactArrayAdapter, times(1)).replaceContacts(same(contacts));
        assertEquals(false, result);
    }

    @Test
    public void whenDisablingUnCheckedBoxes_expectCheckBoxEnabled() {
        ViewGroup mockViewGroup = mock(ViewGroup.class);
        AppCompatCheckBox mockCheckBox = mock(AppCompatCheckBox.class);
        when(mockCheckBox.isChecked()).thenReturn(false);

        when(mockViewGroup.getChildCount()).thenReturn(2);
        when(mockViewGroup.getChildAt(anyInt())).thenReturn(mock(ViewGroup.class)).thenReturn(mockCheckBox);

        inviteMembersPresenter.disableEnableUnCheckedBoxes(false, mockViewGroup);

        verify(mockCheckBox, times(1)).setEnabled(false);

    }

    private Contact[] contacts() {
        return new Contact[0];
    }
}
