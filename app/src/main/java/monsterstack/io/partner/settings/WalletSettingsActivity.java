package monsterstack.io.partner.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import butterknife.BindView;
import butterknife.ButterKnife;
import monsterstack.io.api.UserSessionManager;
import monsterstack.io.api.resources.AuthenticatedUser;
import monsterstack.io.partner.R;

public class WalletSettingsActivity extends DetailSettingsActivity {
    @BindView(R.id.walletAddressEdit)
    EditText walletAddressEdit;

    @BindView(R.id.wallet_list)
    ListView walletList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);

        UserSessionManager userSessionManager = new UserSessionManager(this);
        AuthenticatedUser authenticatedUser = userSessionManager.getUserDetails();

        if(null != authenticatedUser.getWallet()) {
            walletAddressEdit.setText(authenticatedUser.getWallet().getAddress());
        }
    }

    @Override
    public int getContentView() {
        return R.layout.wallet_settings;
    }

    @Override
    public void setUpTransitions() {

    }

    @Override
    public AppCompatActivity getActivity() {
        return this;
    }

    public View.OnClickListener getBackListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WalletSettingsActivity.this.finish();
            }
        };
    }

    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(R.anim.back_slide_right, R.anim.back_slide_left);
    }

    @Override
    public int getActionTitle() {
        return R.string.detail_settings_wallet_id;
    }

}
