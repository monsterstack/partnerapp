package monsterstack.io.partner.common.presenter;

import android.view.Menu;

import java.util.Map;
import java.util.Optional;

import monsterstack.io.partner.common.Control;

public interface Presenter<T extends Control> {
    Presenter<T> present(Optional<Map> metadata);
    Presenter<T> bind(T control);
    boolean onCreateOptionsMenu(Menu menu);

    T getControl();
}
