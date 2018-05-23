package monsterstack.io.partner.services;

import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;

public class AnalyticsService {
    private FirebaseAnalytics firebaseAnalytics;

    public AnalyticsService(Context context) {
        this.firebaseAnalytics = FirebaseAnalytics.getInstance(context);
    }

    public void logPageView(String page) {
        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            Bundle bundle = new Bundle();
            bundle.putString("name", page);
            bundle.putString("userId", FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
            this.firebaseAnalytics.logEvent("page_view", bundle);
        }
    }
}
