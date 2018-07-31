package monsterstack.io.partner.main.presenter.impl;

import android.content.Context;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import monsterstack.io.api.listeners.OnResponseListener;
import monsterstack.io.api.resources.HttpError;
import monsterstack.io.avatarview.AvatarView;
import monsterstack.io.avatarview.User;
import monsterstack.io.partner.adapter.TransactionArrayAdapter;
import monsterstack.io.partner.domain.Member;
import monsterstack.io.partner.domain.Transaction;
import monsterstack.io.partner.main.control.MemberControl;
import monsterstack.io.partner.main.presenter.MemberPresenter;

import static java.lang.Boolean.FALSE;
import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MemberPresenterTest {
    @InjectMocks
    private MemberPresenterImpl memberPresenter;

    @Captor
    private ArgumentCaptor<CharSequence> memberNameCaptor;

    @Captor
    private ArgumentCaptor<User> memberAvatarUserCaptor;

    @Captor
    private ArgumentCaptor<OnResponseListener<Transaction[], HttpError>> transactionCallbackCaptor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        // Prepare Bindings
        memberPresenter.memberNameView = mock(TextView.class);
        memberPresenter.avatarView = mock(AvatarView.class);
        memberPresenter.memberTransactions = mock(RecyclerView.class);
    }

    @Test
    public void whenBinding_expectControllerToBeSet() {
        MemberControl mockControl = mock(MemberControl.class);
        memberPresenter.bind(mockControl);

        assertEquals(mockControl, memberPresenter.getControl());
    }

    @Test
    public void whenPresenting_expectPresenterToMatchCaller() {
        MemberPresenter presenter = (MemberPresenter)memberPresenter.present(Optional.empty());
        assertEquals(memberPresenter, presenter);
    }

    @Test
    public void whenPresenting_expectControlToFindTransactions() {
        // Arrange
        Member mockMember = mock(Member.class);
        when(mockMember.getFullName()).thenReturn("Alfred Adams");
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("member", mockMember);

        MemberControl mockControl = mock(MemberControl.class);
        Context mockContext = mock(Context.class);
        when(mockControl.getContext()).thenReturn(mockContext);
        memberPresenter.bind(mockControl);

        Transaction[] transactions = transactions();
        MemberPresenterImpl spiedOnPresenter = spy(memberPresenter);
        doReturn(mock(DividerItemDecoration.class)).when(spiedOnPresenter).createDividerDecoration(same(mockContext));
        doReturn(mock(TransactionArrayAdapter.class)).when(spiedOnPresenter).createTransactionArrayAdapter(same(mockContext), same(transactions));
        // Act
        MemberPresenter presenter = (MemberPresenter)spiedOnPresenter.present(Optional.of(metadata));

        // Assert
        verify(mockControl, times(1))
                .findTransactionsForMemberGroup(transactionCallbackCaptor.capture());

        transactionCallbackCaptor.getValue().onResponse(transactions, null);

        verify(spiedOnPresenter, times(1)).setMemberFullName(
                same(mockMember.getFullName()));
        verify(spiedOnPresenter, times(1)).setMemberAvatarUser(any(User.class));

        verify(spiedOnPresenter.memberNameView, times(1)).setText(memberNameCaptor.capture());
        verify(spiedOnPresenter.avatarView, times(1)).setUser(memberAvatarUserCaptor.capture());

        CharSequence capturedMemberName = memberNameCaptor.getValue();
        User capturedAvatarUser = memberAvatarUserCaptor.getValue();

        assertEquals("Alfred Adams", capturedMemberName.toString());
        assertEquals("Alfred Adams", capturedAvatarUser.getName());
        assertEquals(spiedOnPresenter, presenter);
    }

    @Test
    public void onCreateMenuOptions_expectReturnFalse() {
        boolean result = memberPresenter.onCreateOptionsMenu(mock(Menu.class));

        assertEquals(FALSE, Boolean.valueOf(result));
    }

    private Transaction[] transactions() {
        return new Transaction[0];
    }
}
