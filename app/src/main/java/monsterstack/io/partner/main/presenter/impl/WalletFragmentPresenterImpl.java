package monsterstack.io.partner.main.presenter.impl;

import android.view.Menu;

import java.util.Map;
import java.util.Optional;

import monsterstack.io.partner.common.presenter.Presenter;
import monsterstack.io.partner.main.control.WalletsFragmentControl;
import monsterstack.io.partner.main.presenter.WalletFragmentPresenter;

public class WalletFragmentPresenterImpl implements WalletFragmentPresenter {
    private WalletsFragmentControl control;

    @Override
    public Presenter<WalletsFragmentControl> present(Optional<Map> metadata) {
        return this;
    }

    @Override
    public Presenter<WalletsFragmentControl> bind(WalletsFragmentControl control) {
        this.control = control;
        return this;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public WalletsFragmentControl getControl() {
        return control;
    }
}
