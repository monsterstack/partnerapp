package monsterstack.io.partner.settings.control;

import monsterstack.io.api.resources.HttpError;
import monsterstack.io.partner.common.Control;

public interface TwoStepVerificationSettingsControl extends Control {
    void finish();

    int getActionTitle();

    void showHttpError(String title, HttpError error);
}
