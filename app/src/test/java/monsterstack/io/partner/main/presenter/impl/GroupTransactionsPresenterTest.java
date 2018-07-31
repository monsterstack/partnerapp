package monsterstack.io.partner.main.presenter.impl;

import android.content.Context;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.View;
import android.widget.ProgressBar;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import monsterstack.io.api.listeners.OnResponseListener;
import monsterstack.io.api.resources.HttpError;
import monsterstack.io.partner.adapter.TransactionArrayAdapter;
import monsterstack.io.partner.domain.Transaction;
import monsterstack.io.partner.main.control.GroupTransactionsControl;
import monsterstack.io.partner.main.presenter.GroupTransactionsPresenter;

import static java.lang.Boolean.FALSE;
import static junit.framework.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GroupTransactionsPresenterTest {
    @InjectMocks
    private GroupTransactionsPresenterImpl groupTransactionsPresenter;

    @Captor
    private ArgumentCaptor<OnResponseListener<Transaction[], HttpError>> transactionCallbackCaptor;

    @Captor
    private ArgumentCaptor<Integer> progressBarVisibilityCaptor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        // Prepare bindings
        groupTransactionsPresenter.groupTransactions = mock(RecyclerView.class);
        groupTransactionsPresenter.progressBar = mock(ProgressBar.class);
    }

    @Test
    public void whenBinding_expectControllerToBeSet() {
        GroupTransactionsControl mockControl = mock(GroupTransactionsControl.class);
        groupTransactionsPresenter.bind(mockControl);

        assertEquals(mockControl, groupTransactionsPresenter.getControl());
    }

    @Test
    public void whenPresenting_expectPresenterToMatchCaller() {
        GroupTransactionsControl mockControl = mock(GroupTransactionsControl.class);
        groupTransactionsPresenter.bind(mockControl);

        GroupTransactionsPresenter presenter = (GroupTransactionsPresenter) groupTransactionsPresenter.present(Optional.empty());

        assertEquals(groupTransactionsPresenter, presenter);
    }

    @Test
    public void whenPresenting_expectControlToFindTransactions() {
        GroupTransactionsControl mockControl = mock(GroupTransactionsControl.class);
        groupTransactionsPresenter.bind(mockControl);
        Transaction[] transactions = transactions();

        GroupTransactionsPresenterImpl spiedOnPresenter = spy(groupTransactionsPresenter);
        Context mockContext = mock(Context.class);
        when(mockControl.getContext()).thenReturn(mockContext);

        doReturn(mock(DividerItemDecoration.class)).when(spiedOnPresenter).createDividerDecoration(same(mockContext));
        doReturn(mock(TransactionArrayAdapter.class)).when(spiedOnPresenter).createTransactionArrayAdapter(same(mockContext), same(transactions));

        GroupTransactionsPresenter presenter = (GroupTransactionsPresenter) spiedOnPresenter.present(Optional.empty());

        verify(mockControl, times(1)).findTransactionsForGroup(transactionCallbackCaptor.capture());

        OnResponseListener<Transaction[], HttpError> capturedCallback = transactionCallbackCaptor.getValue();

        capturedCallback.onResponse(transactions, null);

        assertEquals(spiedOnPresenter, presenter);
    }

    @Test
    public void whenHidingProgressBar_expectVisibilityGone() {
        groupTransactionsPresenter.hideProgressBar();

        verify(groupTransactionsPresenter.progressBar, times(1))
                .setVisibility(progressBarVisibilityCaptor.capture());

        Integer progressBarVisibility = progressBarVisibilityCaptor.getValue();

        assertEquals(View.GONE, progressBarVisibility.intValue());
    }

    @Test
    public void whenShowingProgressBar_expectVisibilityVisible() {
        groupTransactionsPresenter.showProgressBar();

        verify(groupTransactionsPresenter.progressBar, times(1))
                .setVisibility(progressBarVisibilityCaptor.capture());

        Integer progressBarVisibility = progressBarVisibilityCaptor.getValue();

        assertEquals(View.VISIBLE, progressBarVisibility.intValue());
    }

    @Test
    public void onCreateMenuOptions_expectReturnFalse() {
        boolean result = groupTransactionsPresenter.onCreateOptionsMenu(mock(Menu.class));

        assertEquals(FALSE, Boolean.valueOf(result));
    }

    private Transaction[] transactions() {
        return new Transaction[0];
    }
}
