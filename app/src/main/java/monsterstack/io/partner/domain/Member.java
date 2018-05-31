package monsterstack.io.partner.domain;

import java.io.Serializable;
import java.util.UUID;

import lombok.Data;

@Data

public class Member implements Serializable {
    private String id;
    private String firstName;
    private String lastName;
    private Integer slotNumber;
    private String avatar;

    public Member() {
        this.id = UUID.randomUUID().toString();
    }

    public Member(String firstName, String lastName, Integer slotNumber, String avatar) {
        this();
        this.firstName = firstName;
        this.lastName = lastName;
        this.slotNumber = slotNumber;
        this.avatar = avatar;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public static String[] avatars = {
            "https://randomuser.me/api/portraits/women/17.jpg",
            "https://randomuser.me/api/portraits/women/78.jpg",
            "https://randomuser.me/api/portraits/women/45.jpg",
            "https://randomuser.me/api/portraits/men/58.jpg",
            "https://randomuser.me/api/portraits/men/5.jpg",
    };
}
