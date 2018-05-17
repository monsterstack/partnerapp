package monsterstack.io.partner.settings;

import android.support.v7.app.AppCompatActivity;

import monsterstack.io.partner.R;

public class LocalCurrencySettingsActivity extends DetailSettingsActivity {

    public int getContentView() {
        return R.layout.local_currency;
    }


    public void setUpTransitions() {

    }

    @Override
    public int getActionTitle() {
        return R.string.detail_settings_local_currency;
    }
}
