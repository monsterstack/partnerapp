package monsterstack.io.partner.transaction.presenter;

import android.view.Menu;
import android.view.View;
import android.widget.ProgressBar;

import java.util.Map;
import java.util.Optional;

import butterknife.BindView;
import monsterstack.io.partner.R;
import monsterstack.io.partner.common.HasProgressBarSupport;
import monsterstack.io.partner.common.presenter.Presenter;
import monsterstack.io.partner.transaction.control.TransactionControl;

import static android.view.View.GONE;

public class TransactionPresenter implements Presenter<TransactionControl>, HasProgressBarSupport {
    @BindView(R.id.progressbar)
    ProgressBar progressBar;

    private TransactionControl control;

    public TransactionPresenter(TransactionControl control) {
        this.control = control;
    }
    @Override
    public Presenter<TransactionControl> present(Optional<Map> metadata) {
        return this;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(GONE);
    }

    @Override
    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }


    @Override
    public Presenter<TransactionControl> bind(TransactionControl control) {
        this.control = control;
        return this;
    }

    @Override
    public TransactionControl getControl() {
        return control;
    }
}
