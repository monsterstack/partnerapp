package monsterstack.io.partner.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class Contact extends Friend {
    private Boolean alreadyPartnering;

    public static String[] avatars = {
            "https://randomuser.me/api/portraits/women/17.jpg",
            "https://randomuser.me/api/portraits/women/78.jpg",
            "https://randomuser.me/api/portraits/women/45.jpg",
            "https://randomuser.me/api/portraits/men/58.jpg",
            "https://randomuser.me/api/portraits/men/5.jpg",
    };

    public Contact() {
        super();
        this.alreadyPartnering = Boolean.FALSE;
        Integer n = (int)Math.floor(Math.random()* avatars.length);
        setAvatar(avatars[n]);
    }

    public Contact(String fullName, String emailAddress, String phoneNumber) {
        super(fullName, emailAddress, phoneNumber);
        this.alreadyPartnering = Boolean.FALSE;
        Integer n = (int)Math.floor(Math.random()* avatars.length);
        setAvatar(avatars[n]);
    }

    public Contact(String firstName, String lastName, String emailAddress, String phoneNumber) {
        super(firstName, lastName, emailAddress, phoneNumber);
        this.alreadyPartnering = Boolean.FALSE;
        Integer n = (int)Math.floor(Math.random()* avatars.length);
        setAvatar(avatars[n]);
    }

    public Contact alreadyPartnering() {
        this.alreadyPartnering = Boolean.TRUE;
        return this;
    }
}
