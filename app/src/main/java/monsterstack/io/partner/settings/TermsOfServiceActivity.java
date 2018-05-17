package monsterstack.io.partner.settings;

import monsterstack.io.partner.R;

public class TermsOfServiceActivity extends DetailSettingsActivity {
    @Override
    public int getContentView() {
        return R.layout.terms_of_service;
    }

    @Override
    public void setUpTransitions() {

    }

    @Override
    public int getActionTitle() {
        return R.string.about_terms_of_service;
    }


}
