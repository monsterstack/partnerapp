package monsterstack.io.partner.domain;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Friend implements Serializable {
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
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;

        this.fullName = firstName + " " + lastName;
    }

    public Friend(String fullName, String emailAddress, String phoneNumber) {
        this.fullName = fullName;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
    }

    public String getFullName() {
        return this.fullName;
    }
}
