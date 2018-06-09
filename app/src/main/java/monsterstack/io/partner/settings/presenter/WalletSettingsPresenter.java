package monsterstack.io.partner.settings.presenter;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.Map;
import java.util.Optional;

import butterknife.BindView;
import monsterstack.io.api.UserSessionManager;
import monsterstack.io.api.resources.AuthenticatedUser;
import monsterstack.io.partner.common.HasProgressBarSupport;
import monsterstack.io.partner.common.presenter.PresenterAdapter;
import monsterstack.io.partner.R;

public class WalletSettingsPresenter extends PresenterAdapter implements HasProgressBarSupport {
    @BindView(R.id.walletAddressEdit)
    EditText walletAddressEdit;

    @BindView(R.id.wallet_list)
    ListView walletList;

    @BindView(R.id.progressbar)
    ProgressBar progressBar;

    private Context context;

    public WalletSettingsPresenter(Context context) {
        this.context = context;
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
        UserSessionManager userSessionManager = new UserSessionManager(this.context);
        AuthenticatedUser authenticatedUser = userSessionManager.getUserDetails();

        if(null != authenticatedUser.getWallet()) {
            walletAddressEdit.setText(authenticatedUser.getWallet().getAddress());
        }
    }
}
