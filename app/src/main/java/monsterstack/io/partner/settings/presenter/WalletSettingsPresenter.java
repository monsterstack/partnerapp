package monsterstack.io.partner.settings.presenter;

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
import monsterstack.io.partner.common.presenter.PresenterAdapter;
import monsterstack.io.partner.settings.control.WalletSettingsControl;

public class WalletSettingsPresenter extends PresenterAdapter implements HasProgressBarSupport {
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
    public void present(Optional<Map> metadata) {
        UserSessionManager userSessionManager = new UserSessionManager(control.getContext());
        AuthenticatedUser authenticatedUser = userSessionManager.getUserDetails();

        if(null != authenticatedUser.getWallet()) {
            walletAddressEdit.setText(authenticatedUser.getWallet().getAddress());
        }
    }
}
