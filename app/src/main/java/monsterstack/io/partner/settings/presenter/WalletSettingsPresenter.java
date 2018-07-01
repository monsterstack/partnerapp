package monsterstack.io.partner.settings.presenter;

import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.Map;
import java.util.Optional;

import butterknife.BindView;
import monsterstack.io.api.UserSessionManager;
import monsterstack.io.api.resources.AuthenticatedUser;
import monsterstack.io.partner.R;
import monsterstack.io.partner.common.HasProgressBarSupport;
import monsterstack.io.partner.common.presenter.Presenter;
import monsterstack.io.partner.settings.control.WalletSettingsControl;

public class WalletSettingsPresenter implements Presenter<WalletSettingsControl>, HasProgressBarSupport {
    @BindView(R.id.walletAddressEdit)
    EditText walletAddressEdit;

    @BindView(R.id.wallet_list)
    ListView walletList;

    @BindView(R.id.progressbar)
    ProgressBar progressBar;

    private WalletSettingsControl control;

    public WalletSettingsPresenter(WalletSettingsControl control) {
        this.control = control;
    }

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public Presenter<WalletSettingsControl> present(Optional<Map> metadata) {
        UserSessionManager userSessionManager = new UserSessionManager(control.getContext());
        AuthenticatedUser authenticatedUser = userSessionManager.getUserDetails();

        if(null != authenticatedUser.getWallet()) {
            walletAddressEdit.setText(authenticatedUser.getWallet().getAddress());
        }

        return this;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public Presenter<WalletSettingsControl> bind(WalletSettingsControl control) {
        this.control = control;
        return this;
    }

    @Override
    public WalletSettingsControl getControl() {
        return control;
    }
}
