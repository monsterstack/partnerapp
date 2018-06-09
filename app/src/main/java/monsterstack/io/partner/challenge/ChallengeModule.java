package monsterstack.io.partner.challenge;

import dagger.Module;
import dagger.Provides;
import monsterstack.io.partner.challenge.presenter.ChallengeVerificationPresenter;
import monsterstack.io.partner.challenge.presenter.PhoneCapturePresenter;
import monsterstack.io.partner.challenge.presenter.PinCapturePresenter;

@Module
public class ChallengeModule {

    @Provides
    static PinCapturePresenter pinCapturePresenter() {
        return new PinCapturePresenter();
    }

    @Provides
    static PhoneCapturePresenter phoneCapturePresenter() {
        return new PhoneCapturePresenter();
    }

    @Provides
    static ChallengeVerificationPresenter challengeVerificationPresenter() {
        return new ChallengeVerificationPresenter();
    }
}
