package monsterstack.io.partner.menu.presenter;

import android.view.Menu;

import java.util.Map;
import java.util.Optional;

import monsterstack.io.partner.common.presenter.Presenter;
import monsterstack.io.partner.menu.control.BuyCurrencyControl;

public class BuyCurrencyPresenter implements Presenter<BuyCurrencyControl> {
    private BuyCurrencyControl control;

    @Override
    public Presenter<BuyCurrencyControl> present(Optional<Map> metadata) {
        return this;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public Presenter<BuyCurrencyControl> bind(BuyCurrencyControl control) {
        this.control = control;
        return this;
    }

    @Override
    public BuyCurrencyControl getControl() {
        return control;
    }
}
