package monsterstack.io.partner.menu.presenter;

import android.view.Menu;
import android.view.View;
import android.widget.ProgressBar;

import java.util.Map;
import java.util.Optional;

import butterknife.BindView;
import monsterstack.io.partner.R;
import monsterstack.io.partner.common.HasProgressBarSupport;
import monsterstack.io.partner.common.presenter.Presenter;
import monsterstack.io.partner.menu.control.WalletsControl;

public class WalletsPresenter implements Presenter<WalletsControl>, HasProgressBarSupport {
    @BindView(R.id.progressbar)
    ProgressBar progressBar;

    private WalletsControl control;

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public Presenter<WalletsControl> present(Optional<Map> metadata) {
        return this;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public Presenter<WalletsControl> bind(WalletsControl control) {
        this.control = control;
        return this;
    }

    @Override
    public WalletsControl getControl() {
        return control;
    }
}
