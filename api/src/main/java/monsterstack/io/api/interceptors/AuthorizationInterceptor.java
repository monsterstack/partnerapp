package monsterstack.io.api.interceptors;

import android.content.Context;
import android.support.annotation.NonNull;

import java.io.IOException;

import monsterstack.io.api.UserSessionManager;
import monsterstack.io.api.resources.AuthenticatedUser;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthorizationInterceptor implements Interceptor {
    private static final String AUTH_HEADER = "Authorization";
    private static final String CONTENT_TYPE_HEADER = "Content-Type";
    private static final String APPLICATION_JSON = "application/json";

    private static final String BEARER = "Bearer";

    private Context context;
    public AuthorizationInterceptor(Context context) {
        this.context = context;
    }

    public Response intercept(@NonNull Chain chain) throws IOException {
        UserSessionManager sessionManager = new UserSessionManager(this.context);
        AuthenticatedUser user = sessionManager.getUserDetails();

        Request.Builder builder = chain.request().newBuilder();

        Request newRequest;
        builder.addHeader(CONTENT_TYPE_HEADER, APPLICATION_JSON);

        if(null != user.getIdToken()) {
            // how do I get the idToken from the SharedPreferences
            newRequest = builder
                    .addHeader(AUTH_HEADER, BEARER + " " + user.getIdToken())
                    .build();
        } else {
            newRequest = builder
                    .build();
        }
        return chain.proceed(newRequest);
    }
}
