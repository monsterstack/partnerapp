package monsterstack.io.partner.main.presenter.impl;

import android.view.Menu;

import com.github.bassaer.chatmessageview.view.MessageView;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import monsterstack.io.partner.R;
import monsterstack.io.partner.main.control.GroupChatControl;
import monsterstack.io.partner.main.presenter.GroupChatPresenter;

import static java.lang.Boolean.FALSE;
import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class GroupChatPresenterTest {
    @InjectMocks
    private GroupChatPresenterImpl groupChatPresenter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        // Prepare Bindings
        groupChatPresenter.chatView = mock(MessageView.class);
    }

    @Test
    public void whenBinding_expectControllerToBeSet() {
        GroupChatControl mockControl = mock(GroupChatControl.class);
        groupChatPresenter.bind(mockControl);

        assertEquals(mockControl, groupChatPresenter.getControl());
    }

    @Test
    public void whenPresenting_expectPresenterToMatchCaller() {
        GroupChatPresenterImpl spiedOnPresenter = spy(groupChatPresenter);
        doReturn(R.color.black).when(spiedOnPresenter).getChatBackgroundColor();
        doReturn(R.color.black).when(spiedOnPresenter).getChatRightBubbleColor();

        GroupChatPresenter presenter = (GroupChatPresenter) spiedOnPresenter.present(Optional.empty());

        assertEquals(spiedOnPresenter, presenter);
    }

    @Test
    public void onCreateMenuOptions_expectReturnFalse() {
        boolean result = groupChatPresenter.onCreateOptionsMenu(mock(Menu.class));

        assertEquals(FALSE, Boolean.valueOf(result));
    }
}
