package monsterstack.io.partner.registration.presenter;

import monsterstack.io.api.resources.Registration;
import monsterstack.io.partner.common.HasProgressBarSupport;
import monsterstack.io.partner.common.HasSnackBarSupport;
import monsterstack.io.partner.common.presenter.Presenter;
import monsterstack.io.partner.registration.control.RegistrationEmailCaptureControl;

public interface RegistrationEmailCapturePresenter extends Presenter<RegistrationEmailCaptureControl>,
        HasProgressBarSupport, HasSnackBarSupport {
    void markEmailBad();
    void markEmailGood();

    Registration buildRegistrationFromBindingData();
    String getEmailAddress();
    Integer getProgress();
}
