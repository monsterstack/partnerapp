package monsterstack.io.partner.settings;

import android.support.v7.app.AppCompatActivity;

import monsterstack.io.partner.R;

public class AboutUsActivity extends DetailSettingsActivity {
    @Override
    public int getContentView() {
        return R.layout.about_us;
    }

    @Override
    public void setUpTransitions() {

    }

    @Override
    public AppCompatActivity getActivity() {
        return this;
    }

    @Override
    public int getActionTitle() {
        return R.string.about_about_us;
    }


}
