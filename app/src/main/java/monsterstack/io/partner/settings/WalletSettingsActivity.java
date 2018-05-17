package monsterstack.io.partner.settings;

import android.support.v7.app.AppCompatActivity;
import android.view.View;

import monsterstack.io.partner.R;

public class WalletSettingsActivity extends DetailSettingsActivity {
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
