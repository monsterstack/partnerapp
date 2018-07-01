package monsterstack.io.partner;

import monsterstack.io.partner.main.module.DaggerMainComponent;
import monsterstack.io.partner.main.module.MainComponent;
import monsterstack.io.partner.main.module.MainModule;
import monsterstack.io.partner.services.module.ServicesModule;

public class Application extends android.app.Application {
    private MainComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        setupGraph();
    }

    private void setupGraph() {
        component = DaggerMainComponent.builder()
                .mainModule(new MainModule(getApplicationContext()))
                .servicesModule(new ServicesModule(getApplicationContext()))
                .build();
    }

    public MainComponent component() {
        return component;
    }
}
