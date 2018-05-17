package monsterstack.io.api;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigValue;

public class RemoteConfigurationService {

    private static FirebaseRemoteConfig instance;

    public void fetch(Long expiry) {
        Task<Void> result = getInstance().fetch(expiry);

        result.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                getInstance().activateFetched();
            }
        });
    }

    public FirebaseRemoteConfigValue getValue(String key, String namespace) {
        return getInstance().getValue(key, namespace);
    }

    private FirebaseRemoteConfig getInstance() {
        if(instance == null) {
            instance = FirebaseRemoteConfig.getInstance();
        }

        return instance;
    }
}
