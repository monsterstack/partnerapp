package monsterstack.io.partner.menu.module;

import dagger.Module;
import dagger.Provides;
import monsterstack.io.partner.menu.presenter.WalletsPresenter;
import monsterstack.io.partner.settings.presenter.TwoStepVerificationSettingsPresenter;

@Module
public class MenuModule {

    @Provides
    static TwoStepVerificationSettingsPresenter twoStepVerificationSettingsPresenter() {
        return new TwoStepVerificationSettingsPresenter();
    }

    @Provides
    static WalletsPresenter walletsPresenter() {
        return new WalletsPresenter();
    }
}
