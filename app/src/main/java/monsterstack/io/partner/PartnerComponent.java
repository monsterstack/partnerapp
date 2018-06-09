package monsterstack.io.partner;

import dagger.BindsInstance;
import dagger.Component;
import monsterstack.io.partner.challenge.ChallengeModule;

@Component(modules = {
        ChallengeModule.class
})
@PartnerApplicationScope
public interface PartnerComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);
        PartnerComponent build();
    }

    void inject(Application app);
}
