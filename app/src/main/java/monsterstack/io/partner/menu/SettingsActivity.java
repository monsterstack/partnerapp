package monsterstack.io.partner.menu;

import android.os.Bundle;

import monsterstack.io.partner.Application;
import monsterstack.io.partner.R;

public class SettingsActivity extends PreferenceMenuActivity {

    public SettingsActivity() {
        super();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ((Application)getApplication()).component().inject(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getContentView() {
        return R.layout.settings;
    }

    public int getActionTitle() {
        return R.string.settings;
    }
}
