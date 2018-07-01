package monsterstack.io.partner.menu.presenter;

import android.view.Menu;

import java.util.Map;
import java.util.Optional;

import monsterstack.io.partner.common.presenter.Presenter;
import monsterstack.io.partner.menu.control.BackupControl;

public class BackupPresenter implements Presenter<BackupControl> {
    private BackupControl control;

    @Override
    public Presenter<BackupControl> present(Optional<Map> metadata) {
        return this;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public Presenter<BackupControl> bind(BackupControl control) {
        this.control = control;
        return this;
    }

    @Override
    public BackupControl getControl() {
        return control;
    }
}
