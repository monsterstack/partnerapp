package monsterstack.io.api.interceptors;

import android.content.Context;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import java.io.IOException;

import monsterstack.io.api.UserSessionManager;
import monsterstack.io.api.resources.AuthenticatedUser;
import okhttp3.Interceptor;
import okhttp3.Response;

public class RefreshIdTokenInterceptor implements Interceptor {
    private Context context;

    public RefreshIdTokenInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        final UserSessionManager sessionManager = new UserSessionManager(this.context);
        final AuthenticatedUser authenticatedUser = sessionManager.getUserDetails();

        if(null != authenticatedUser.getId()) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            Task<GetTokenResult> getTokenResultTask = user.getIdToken(true);
            getTokenResultTask.addOnSuccessListener(new OnSuccessListener<GetTokenResult>() {
                @Override
                public void onSuccess(GetTokenResult getTokenResult) {
                    String token = getTokenResult.getToken();
                    authenticatedUser.setIdToken(token);
                    sessionManager.createUserSession(authenticatedUser);
                }
            });
        }

        return chain.proceed(chain.request());
    }
}
