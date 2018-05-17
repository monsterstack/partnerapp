package monsterstack.io.partner.settings;

import monsterstack.io.partner.R;

public class PrivacyPolicyActivity extends DetailSettingsActivity {
    @Override
    public int getContentView() {
        return R.layout.privacy_policy;
    }

    @Override
    public void setUpTransitions() {

    }

    @Override
    public int getActionTitle() {
        return R.string.about_privacy_policy;
    }
}
