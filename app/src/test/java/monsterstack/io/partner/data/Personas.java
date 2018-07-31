package monsterstack.io.partner.data;

import android.content.Context;

import com.google.firebase.FirebaseApp;

import java.util.UUID;

import lombok.Getter;
import monsterstack.io.api.UserSessionManager;
import monsterstack.io.api.resources.AuthenticatedUser;

public class Personas {

    public Molly asMolly() {
        return new Molly();
    }

    @Getter
    public class Molly implements Persona {
        private String firstName;
        private String lastName;
        private String avatarUrl;
        private String phoneNumber;
        private String emailAddress;

        private AuthenticatedUser authenticatedUser;

        public Molly() {
            this.firstName = "Molly";
            this.lastName = "Pearson";
            this.avatarUrl = "https://randomuser.me/api/portraits/women/14.jpg";
            this.phoneNumber = "+1 954 678 9999";
            this.emailAddress = String.format("molly+%s@gmail.com", UUID.randomUUID().toString());
        }


        public Molly authenticate(Context context) {
            AuthenticatedUser authenticatedUser = new AuthenticatedUser();
            authenticatedUser.setAvatarUrl(this.avatarUrl);
            authenticatedUser.setFirstName(this.firstName);
            authenticatedUser.setLastName(this.lastName);
            authenticatedUser.setPhoneNumber(this.phoneNumber);
            authenticatedUser.setEmailAddress(this.emailAddress);
            authenticatedUser.setIdToken(UUID.randomUUID().toString());
            authenticatedUser.setPushRegistrationToken(UUID.randomUUID().toString());

            this.authenticatedUser = authenticatedUser;

            UserSessionManager userSessionManager = new UserSessionManager(context);
            userSessionManager.createUserSession(this.authenticatedUser);

            FirebaseApp.initializeApp(context);

            return this;
        }

        public String getFullName() {
            return String.format("%s %s", this.firstName, this.lastName);
        }
    }
}
