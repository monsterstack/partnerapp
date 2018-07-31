package monsterstack.io.partner.presenter;

import monsterstack.io.api.resources.AuthenticatedUser;
import monsterstack.io.partner.common.presenter.Presenter;
import monsterstack.io.partner.control.MainControl;

public interface MainPresenter extends Presenter<MainControl> {
    void updateAvatarWithAuthenticatedUser(AuthenticatedUser authenticatedUser);
    void closeDrawer();
}
