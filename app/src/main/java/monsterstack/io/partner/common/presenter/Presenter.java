package monsterstack.io.partner.common.presenter;

import android.view.Menu;

import java.util.Map;
import java.util.Optional;

public interface Presenter {
    void present(Optional<Map> metadata);
    boolean onCreateOptionsMenu(Menu menu);
}
