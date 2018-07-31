package monsterstack.io.partner.main.presenter.impl;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.leinardi.android.speeddial.SpeedDialView;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.reflect.Whitebox;

import java.util.Currency;
import java.util.Locale;
import java.util.Optional;

import monsterstack.io.api.listeners.OnResponseListener;
import monsterstack.io.api.resources.HttpError;
import monsterstack.io.partner.R;
import monsterstack.io.partner.adapter.GroupAdapter;
import monsterstack.io.partner.domain.Group;
import monsterstack.io.partner.main.control.GroupFragmentControl;
import monsterstack.io.partner.main.presenter.GroupFragmentPresenter;
import monsterstack.io.streamview.PausingViewPager;

import static java.lang.Boolean.FALSE;
import static junit.framework.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GroupFragmentPresenterTest {
    @InjectMocks
    private GroupFragmentPresenterImpl groupFragmentPresenter;

    @Mock
    private GroupFragmentControl mockControl;
    @Captor
    private ArgumentCaptor<OnResponseListener<Group[], HttpError>> onResponseListenerArgumentCaptor;

    private Context mockContext;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        mockContext = mock(Context.class);
        when(mockControl.getContext()).thenReturn(mockContext);

        // Prepare bindings
        groupFragmentPresenter.createImageButton = mock(Button.class);
        groupFragmentPresenter.getStartedCard = mock(LinearLayout.class);
        groupFragmentPresenter.progressBar = mock(ProgressBar.class);
        groupFragmentPresenter.speedDialView = mock(SpeedDialView.class);
        groupFragmentPresenter.createImageButton = mock(Button.class);
        groupFragmentPresenter.viewPager = mock(PausingViewPager.class);
        groupFragmentPresenter.cardAdapter = mock(GroupAdapter.class);
    }

    @Test
    public void whenBinding_expectControllerToBeSet() {
        GroupFragmentControl mockControl = mock(GroupFragmentControl.class);
        groupFragmentPresenter.bind(mockControl);

        assertEquals(mockControl, groupFragmentPresenter.getControl());
    }

    @Test
    public void whenPresentingNoGroups_expectPresenterToShowGetStartedCard() {
        GroupFragmentPresenter presenter = (GroupFragmentPresenter) groupFragmentPresenter.present(Optional.empty());

        verify(mockControl, times(1))
                .findGroupsAssociatedWithUser(onResponseListenerArgumentCaptor.capture());

        OnResponseListener<Group[], HttpError> onResponseListener = onResponseListenerArgumentCaptor.getValue();
        onResponseListener.onResponse(noGroups(), null);

        verify(groupFragmentPresenter.getStartedCard, times(1)).setVisibility(View.VISIBLE);
        verify(groupFragmentPresenter.progressBar, times(1)).setVisibility(View.GONE);
        assertEquals(groupFragmentPresenter, presenter);
    }

    @Test
    public void whenPresentingGroups_expectPresenterToPrepareCardAdapter() {
        Group[] groups = groups();
        GroupFragmentPresenterImpl spiedOnPresenter = spy(groupFragmentPresenter);

        doNothing().when(spiedOnPresenter).prepareViewPager(any(ViewPager.class), any(GroupAdapter.class));

        GroupFragmentPresenter presenter = (GroupFragmentPresenter) spiedOnPresenter.present(Optional.empty());

        verify(mockControl, times(1))
                .findGroupsAssociatedWithUser(onResponseListenerArgumentCaptor.capture());

        OnResponseListener<Group[], HttpError> onResponseListener = onResponseListenerArgumentCaptor.getValue();
        onResponseListener.onResponse(groups, null);

        verify(spiedOnPresenter.speedDialView, times(1)).setVisibility(View.VISIBLE);
        verify(spiedOnPresenter.progressBar, times(1)).setVisibility(View.GONE);

        verify(mockControl, times(1)).inflateFloatingActionButton(same(groups));
        verify(mockControl, times(1)).listenForCardViewAnimationEvents(any(GroupAdapter.class));

        verify(spiedOnPresenter, times(1)).prepareViewPager(
                same(spiedOnPresenter.viewPager), same(spiedOnPresenter.cardAdapter)
        );
        assertEquals(spiedOnPresenter, presenter);
    }

    @Test
    public void onCreateMenuOptions_expectReturnFalse() {
        boolean result = groupFragmentPresenter.onCreateOptionsMenu(mock(Menu.class));

        assertEquals(FALSE, Boolean.valueOf(result));
    }

    @Test
    public void whenPausingViewPager_expectPause() {
        groupFragmentPresenter.pauseViewPager();

        verify(groupFragmentPresenter.viewPager, times(1)).setPaused(true);
    }

    @Test
    public void whenUnpausingViewPager_expectUnpause() {
        groupFragmentPresenter.unpauseViewPager();

        verify(groupFragmentPresenter.viewPager, times(1)).setPaused(false);
    }

    @Test
    public void whenHidingSpeedDialView_expectHide() {
        groupFragmentPresenter.hideSpeedDialView();

        verify(groupFragmentPresenter.speedDialView, times(1)).hide();
    }

    @Test
    public void whenShowingSpeedDialView_expectShow() {
        groupFragmentPresenter.showSpeedDialView();

        verify(groupFragmentPresenter.speedDialView, times(1)).show();
    }

    @Test
    public void whenInflatingSpeedDialView_expectInflate() {
        ArgumentCaptor<Integer> resourceLayoutCaptor = ArgumentCaptor.forClass(Integer.class);
        groupFragmentPresenter.inflateSpeedDial();

        verify(groupFragmentPresenter.speedDialView, times(1))
                .inflate(resourceLayoutCaptor.capture());

        Integer capturedLayoutResource = resourceLayoutCaptor.getValue();

        assertEquals(R.menu.group_view_speed_dial, capturedLayoutResource.intValue());
    }

    @Test
    public void onRefreshGroups_expectReplaceGroupsInGroupAdapter() {
        Group[] groups = groups();

        GroupFragmentPresenterImpl spiedOnPresenter = spy(groupFragmentPresenter);
        // ignore this call for now
        doNothing().when(spiedOnPresenter).prepareCardAdapter(any(Group[].class));

        spiedOnPresenter.refreshGroups();

        verify(spiedOnPresenter, times(1)).showProgressBar();

        verify(mockControl, times(1)).findGroupsAssociatedWithUser(onResponseListenerArgumentCaptor.capture());

        OnResponseListener<Group[], HttpError> onResponseListener = onResponseListenerArgumentCaptor.getValue();

        onResponseListener.onResponse(groups, null);

        verify(spiedOnPresenter.cardAdapter, times(1)).replaceGroups(same(groups));
    }

    @Test
    public void givenHttpError_onRefreshGroups_expectShowHttpError() {
        GroupFragmentPresenterImpl spiedOnPresenter = spy(groupFragmentPresenter);
        // ignore this call for now
        doNothing().when(spiedOnPresenter).showHttpError(anyString(), anyString(), any(HttpError.class));

        spiedOnPresenter.refreshGroups();

        verify(spiedOnPresenter, times(1)).showProgressBar();

        verify(mockControl, times(1)).findGroupsAssociatedWithUser(onResponseListenerArgumentCaptor.capture());

        OnResponseListener<Group[], HttpError> onResponseListener = onResponseListenerArgumentCaptor.getValue();

        onResponseListener.onResponse(null, mock(HttpError.class));

        verify(spiedOnPresenter, times(1)).showHttpError(
                anyString(), anyString(), any(HttpError.class)
        );
    }

    @Test
    public void givenNullGroupAdapter_onRefreshGroups_expectPrepareCardAdapter() {
        Resources mockResources = mock(Resources.class);
        DisplayMetrics mockDisplayMetrics = new DisplayMetrics();
        mockDisplayMetrics.density = 10f;
        mockDisplayMetrics.xdpi = 100f;
        mockDisplayMetrics.densityDpi = 10;
        mockDisplayMetrics.scaledDensity = 10;

        ArgumentCaptor<ViewPager.OnPageChangeListener> onPageChangeListenerArgumentCaptor =
                ArgumentCaptor.forClass(ViewPager.OnPageChangeListener.class);

        when(mockResources.getDisplayMetrics()).thenReturn(mockDisplayMetrics);
        when(mockContext.getResources()).thenReturn(mockResources);

        Group[] groups = groups();
        GroupFragmentPresenterImpl spiedOnPresenter = spy(groupFragmentPresenter);
        doNothing().when(spiedOnPresenter).prepareCardAdapter(same(groups));
        Whitebox.setInternalState(spiedOnPresenter, "groupArray", groups);
        spiedOnPresenter.cardAdapter = null;

        spiedOnPresenter.refreshGroups();

        verify(mockControl, times(1)).findGroupsAssociatedWithUser(onResponseListenerArgumentCaptor.capture());

        OnResponseListener<Group[], HttpError> onResponseListener = onResponseListenerArgumentCaptor.getValue();

        onResponseListener.onResponse(groups, null);

        verify(spiedOnPresenter, times(1)).prepareCardAdapter(same(groups));
    }

    @Test
    public void whenPreparingCardView_expectCardSetupCorrectly() {
        Group[] groups = groups();
        GroupFragmentPresenterImpl spiedOnPresenter = spy(groupFragmentPresenter);
        doReturn(10).when(spiedOnPresenter).getCardMargin();

        spiedOnPresenter.prepareCardAdapter(groups);

        verify(spiedOnPresenter.getStartedCard, times(1)).setVisibility(View.GONE);
        verify(spiedOnPresenter.speedDialView, times(1)).setVisibility(View.VISIBLE);

        verify(mockControl, times(1)).listenForSpeedDialSelections(same(spiedOnPresenter.speedDialView));
        verify(mockControl, times(1)).inflateFloatingActionButton(same(groups));
    }

    private Group[] noGroups() {
        return new Group[0];
    }

    private Group[] groups() {
        return new Group[] {
                new Group("Felix Monroe", 12,
                        Currency.getInstance(Locale.US), 500.00, 25.00)
        };
    }
}
