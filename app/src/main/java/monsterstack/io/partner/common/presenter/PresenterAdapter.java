package monsterstack.io.partner.common.presenter;

import android.view.Menu;

import java.util.Map;
import java.util.Optional;

public class PresenterAdapter implements Presenter {
    @Override
    public void present(Optional<Map> metadata) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }
}
