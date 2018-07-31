package monsterstack.io.partner.registration.presenter;

import monsterstack.io.api.resources.Registration;
import monsterstack.io.partner.common.HasProgressBarSupport;
import monsterstack.io.partner.common.HasSnackBarSupport;
import monsterstack.io.partner.common.presenter.Presenter;
import monsterstack.io.partner.registration.control.RegistrationNameCaptureControl;

public interface RegistrationNameCapturePresenter extends Presenter<RegistrationNameCaptureControl>,
        HasProgressBarSupport, HasSnackBarSupport {
    Registration buildRegistrationFromBindingData();

    String getFirstName();
    String getLastName();
}
