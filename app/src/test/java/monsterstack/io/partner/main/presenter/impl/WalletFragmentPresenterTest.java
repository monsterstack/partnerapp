package monsterstack.io.partner.main.presenter.impl;

import android.view.Menu;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import monsterstack.io.partner.main.control.WalletsFragmentControl;
import monsterstack.io.partner.main.presenter.WalletFragmentPresenter;

import static java.lang.Boolean.FALSE;
import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class WalletFragmentPresenterTest {
    @InjectMocks
    private WalletFragmentPresenterImpl walletFragmentPresenter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void whenBinding_expectControllerToBeSet() {
        WalletsFragmentControl mockControl = mock(WalletsFragmentControl.class);
        walletFragmentPresenter.bind(mockControl);

        assertEquals(mockControl, walletFragmentPresenter.getControl());
    }

    @Test
    public void whenPresenting_expectPresenterToMatchCaller() {
        WalletFragmentPresenter presenter = (WalletFragmentPresenter)walletFragmentPresenter.present(Optional.empty());

        assertEquals(walletFragmentPresenter, presenter);
    }

    @Test
    public void onCreateMenuOptions_expectReturnFalse() {
        boolean result = walletFragmentPresenter.onCreateOptionsMenu(mock(Menu.class));

        assertEquals(FALSE, Boolean.valueOf(result));
    }
}
