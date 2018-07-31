package monsterstack.io.partner.domain;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Friend implements Serializable {
    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    private String fullName;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String phoneNumber;

    private String avatar;

    public static String[] avatars = {
            "https://randomuser.me/api/portraits/women/17.jpg",
            "https://randomuser.me/api/portraits/women/78.jpg",
            "https://randomuser.me/api/portraits/women/45.jpg",
            "https://randomuser.me/api/portraits/men/58.jpg",
            "https://randomuser.me/api/portraits/men/5.jpg",
    };

    public Friend(String firstName, String lastName, String emailAddress, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = validEmailOrNull(emailAddress);
        this.phoneNumber = phoneNumber;

        this.fullName = firstName + " " + lastName;
    }

    public Friend(String fullName, String emailAddress, String phoneNumber) {
        this.fullName = fullName;
        this.emailAddress = validEmailOrNull(emailAddress);
        this.phoneNumber = phoneNumber;
    }

    public String getFullName() {
        return this.fullName;
    }

    public String validEmailOrNull(String emailAddress) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailAddress);

        if(!matcher.find()) {
            return null;
        }

        return emailAddress;
    }
}
