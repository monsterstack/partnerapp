package monsterstack.io.partner.main.module;

import android.content.Context;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import monsterstack.io.partner.PresenterFactory;

@Module
public class MainModule {
    private Context context;

    public MainModule(Context context) {
        this.context = context;
    }

    @Provides
    @Named("presenterFactory")
    static PresenterFactory presenterFactory(PresenterFactory presenterFactory) {
        return presenterFactory;
    }

    @Provides
    @Named("applicationContext")
    public Context context() {
        return context;
    }

}
