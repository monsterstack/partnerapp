package monsterstack.io.api;

import android.content.Context;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import monsterstack.io.api.custom.AuthServiceCustom;
import monsterstack.io.api.custom.ChallengeServiceCustom;
import monsterstack.io.api.custom.RegistrationServiceCustom;
import monsterstack.io.api.custom.UserServiceCustom;
import monsterstack.io.api.custom.impl.AuthServiceCustomImpl;
import monsterstack.io.api.custom.impl.ChallengeServiceCustomImpl;
import monsterstack.io.api.custom.impl.RegistrationServiceCustomImpl;
import monsterstack.io.api.custom.impl.UserServiceCustomImpl;
import monsterstack.io.api.interceptors.AuthorizationInterceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class ServiceLocator {
    private Retrofit retrofit;
    private Map<String, Object> services;

    private static ServiceLocator instance;

    public static ServiceLocator getInstance(Context context) {
        if(null == instance) {
            try {
                instance = new ServiceLocator(context, new URL(context.getString(R.string.api_base_url)));
            } catch (IOException ioException) {
                throw new RuntimeException(ioException);
            }
        }
        return instance;
    }

    ServiceLocator(Context context, URL baseUrl) {
        this.retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl.toString())
                .client(new OkHttpClient.Builder().addInterceptor(new AuthorizationInterceptor(context)).build())
                .addConverterFactory(JacksonConverterFactory.create())
                .build();

        this.services = new HashMap<>();
    }

    public PartnerService getPartnerService() {
        return (PartnerService)retrieveService(PartnerService.class);
    }

    public UserServiceCustom getUserService() {
        UserService userService = (UserService)retrieveService(UserService.class);
        return new UserServiceCustomImpl(userService);
    }

    public FriendService getFriendService() {
        return (FriendService)retrieveService(FriendService.class);
    }

    public MemberService getMemberService() {
        return (MemberService) retrieveService(MemberService.class);
    }

    public InvitationService getInvitationService() {
        return (InvitationService) retrieveService(InvitationService.class);
    }

    public RegistrationServiceCustom getRegistrationService() {
        RegistrationService registrationService = (RegistrationService) retrieveService(RegistrationService.class);
        return new RegistrationServiceCustomImpl(registrationService, getUserService());
    }

    public TransactionService getTransactionService() {
        return (TransactionService) retrieveService(TransactionService.class);
    }

    public ChallengeServiceCustom getChallengeService() {
        ChallengeService challengeService = (ChallengeService) retrieveService(ChallengeService.class);
        return new ChallengeServiceCustomImpl(challengeService);
    }

    public AuthServiceCustom getAuthService() {
        AuthService authService = (AuthService) retrieveService(AuthService.class);
        UserService userService = (UserService) retrieveService(UserService.class);
        return new AuthServiceCustomImpl(authService, userService);
    }

    public RemoteConfigurationService getRemoteConfigurationService() {
        return new RemoteConfigurationService();
    }

    private Object retrieveService(Class clazz) {
        if(services.containsKey(clazz.getCanonicalName())) {
            return services.get(clazz.getCanonicalName());
        } else {
            Object service = this.retrofit.create(clazz);
            services.put(clazz.getCanonicalName(), service);

            return service;
        }
    }
}
