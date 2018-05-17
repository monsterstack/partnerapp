package monsterstack.io.api;

import org.junit.Before;
import org.junit.Test;

import java.net.URL;

import static org.junit.Assert.assertNotNull;

public class ServiceLocatorTest {

    @Before
    public void setUp() {

    }

    @Test
    public void whenRetrievingPartnerService_expectNotNull() throws Exception {
        ServiceLocator serviceLocator = new ServiceLocator(new URL(TestConfig.BASE_URL));

        PartnerService partnerService = serviceLocator.getPartnerService();

        assertNotNull(partnerService);
    }

    @Test
    public void whenRetrievingUserService_expectNotNull() throws Exception {
        ServiceLocator serviceLocator = new ServiceLocator(new URL(TestConfig.BASE_URL));

        UserService userService = serviceLocator.getUserService();

        assertNotNull(userService);
    }

    @Test
    public void whenRetrievingFriendService_expectNotNull() throws Exception {
        ServiceLocator serviceLocator = new ServiceLocator(new URL(TestConfig.BASE_URL));

        FriendService friendService = serviceLocator.getFriendService();

        assertNotNull(friendService);
    }

    @Test
    public void whenRetrievingMemberService_expectNotNull() throws Exception {
        ServiceLocator serviceLocator = new ServiceLocator(new URL(TestConfig.BASE_URL));

        MemberService memberService = serviceLocator.getMemberService();

        assertNotNull(memberService);
    }

    @Test
    public void whenRetrievingTransactionService_expectNotNull() throws Exception {
        ServiceLocator serviceLocator = new ServiceLocator(new URL(TestConfig.BASE_URL));

        TransactionService transactionService = serviceLocator.getTransactionService();

        assertNotNull(transactionService);
    }

    @Test
    public void whenRetrievingRegistrationService_expectNotNull() throws Exception {
        ServiceLocator serviceLocator = new ServiceLocator(new URL(TestConfig.BASE_URL));

        RegistrationService registrationService = serviceLocator.getRegistrationService();

        assertNotNull(registrationService);
    }

    @Test
    public void whenRetrievingInvitationService_expectNotNull() throws Exception {
        ServiceLocator serviceLocator = new ServiceLocator(new URL(TestConfig.BASE_URL));

        InvitationService invitationService = serviceLocator.getInvitationService();

        assertNotNull(invitationService);
    }

    @Test
    public void whenRetrievingChallengeService_expectNotNull() throws Exception {
        ServiceLocator serviceLocator = new ServiceLocator(new URL(TestConfig.BASE_URL));

        ChallengeService challengeService = serviceLocator.getChallengeService();

        assertNotNull(challengeService);
    }

    @Test
    public void whenRetrievingAuthService_expectNotNull() throws Exception {
        ServiceLocator serviceLocator = new ServiceLocator(new URL(TestConfig.BASE_URL));

        AuthService authService = serviceLocator.getAuthService();

        assertNotNull(authService);
    }

    @Test
    public void whenRetrievingRemoteConfigurationService_expectNotNull() throws Exception {
        ServiceLocator serviceLocator = new ServiceLocator(new URL(TestConfig.BASE_URL));

        RemoteConfigurationService remoteConfigurationService = serviceLocator.getRemoteConfigurationService();

        assertNotNull(remoteConfigurationService);
    }
}
