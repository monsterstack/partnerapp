package monsterstack.io.partner.services.module;

import android.content.Context;

import java.net.MalformedURLException;
import java.net.URL;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import monsterstack.io.api.InvitationService;
import monsterstack.io.api.PartnerService;
import monsterstack.io.api.ServiceLocator;
import monsterstack.io.api.custom.UserServiceCustom;
import monsterstack.io.partner.BuildConfig;
import monsterstack.io.partner.services.AnalyticsService;
import monsterstack.io.partner.services.MessagingService;
import monsterstack.io.partner.services.PartnerFirebaseInstanceIdService;
import monsterstack.io.partner.services.PermissionsService;

@Module
public class ServicesModule {
    private Context context;

    public ServicesModule(Context serviceContext) {
        this.context = serviceContext;
    }

    @Provides
    static MessagingService messagingService(@Named("serviceContext") Context context,
                                             PartnerFirebaseInstanceIdService partnerFirebaseInstanceIdService) {
        return new MessagingService(context, partnerFirebaseInstanceIdService);
    }

    @Provides
    static AnalyticsService analyticsService(@Named("serviceContext") Context context) {
        return new AnalyticsService(context);
    }

    @Provides
    static PartnerFirebaseInstanceIdService partnerFirebaseInstanceIdService(ServiceLocator serviceLocator) {
        return new PartnerFirebaseInstanceIdService(serviceLocator);
    }

    @Provides
    @Singleton
    static ServiceLocator serviceLocator(@Named("serviceContext") Context context, @Named("baseUrl") URL baseUrl) {
        return new ServiceLocator(context, baseUrl);
    }

    @Provides
    @Singleton
    static PartnerService partnerService(ServiceLocator serviceLocator) {
        return serviceLocator.getPartnerService();
    }

    @Provides
    @Singleton
    static UserServiceCustom userService(ServiceLocator serviceLocator) {
        return serviceLocator.getUserService();
    }

    @Provides
    @Singleton
    static InvitationService invitationService(ServiceLocator serviceLocator) {
        return serviceLocator.getInvitationService();
    }

    @Provides
    @Singleton
    static PermissionsService permissionsService(@Named("serviceContext") Context context) {
        return new PermissionsService(context);
    }

    @Provides @Named("serviceContext") Context context() {
        return context;
    }

    @Provides
    @Singleton
    @Named("baseUrl")
    public URL baseUrl() throws RuntimeException {
        try {
            return new URL(BuildConfig.FB_API_BASE_URL);
        } catch (MalformedURLException malformedUrlException) {
            throw new RuntimeException(malformedUrlException);
        }
    }
}
