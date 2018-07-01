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
            "https://randomuser.me/api/portraits/women/1.jpg",
            "https://randomuser.me/api/portraits/women/2.jpg",
            "https://randomuser.me/api/portraits/women/3.jpg",
            "https://randomuser.me/api/portraits/women/4.jpg",
            "https://randomuser.me/api/portraits/women/5.jpg",
            "https://randomuser.me/api/portraits/women/6.jpg",
            "https://randomuser.me/api/portraits/women/7.jpg",
            "https://randomuser.me/api/portraits/women/8.jpg",
            "https://randomuser.me/api/portraits/women/9.jpg",
            "https://randomuser.me/api/portraits/women/10.jpg",
            "https://randomuser.me/api/portraits/women/11.jpg",
            "https://randomuser.me/api/portraits/women/12.jpg",
            "https://randomuser.me/api/portraits/women/13.jpg",
            "https://randomuser.me/api/portraits/women/14.jpg",
            "https://randomuser.me/api/portraits/women/15.jpg",
            "https://randomuser.me/api/portraits/women/16.jpg",
            "https://randomuser.me/api/portraits/women/17.jpg",
            "https://randomuser.me/api/portraits/women/18.jpg",
            "https://randomuser.me/api/portraits/women/19.jpg",
            "https://randomuser.me/api/portraits/women/20.jpg",
            "https://randomuser.me/api/portraits/women/21.jpg",
            "https://randomuser.me/api/portraits/women/22.jpg",
            "https://randomuser.me/api/portraits/women/23.jpg",
            "https://randomuser.me/api/portraits/women/24.jpg",
            "https://randomuser.me/api/portraits/women/25.jpg",
            "https://randomuser.me/api/portraits/women/26.jpg",
            "https://randomuser.me/api/portraits/women/27.jpg",
            "https://randomuser.me/api/portraits/women/28.jpg",
            "https://randomuser.me/api/portraits/women/29.jpg",
            "https://randomuser.me/api/portraits/women/30.jpg",
            "https://randomuser.me/api/portraits/women/41.jpg",
            "https://randomuser.me/api/portraits/women/42.jpg",
            "https://randomuser.me/api/portraits/women/43.jpg",
            "https://randomuser.me/api/portraits/women/44.jpg",
            "https://randomuser.me/api/portraits/women/45.jpg",
            "https://randomuser.me/api/portraits/women/46.jpg",
            "https://randomuser.me/api/portraits/women/47.jpg",
            "https://randomuser.me/api/portraits/women/48.jpg",
            "https://randomuser.me/api/portraits/women/49.jpg",
            "https://randomuser.me/api/portraits/women/50.jpg",
            "https://randomuser.me/api/portraits/women/78.jpg",
            "https://randomuser.me/api/portraits/women/45.jpg",
            "https://randomuser.me/api/portraits/women/51.jpg",
            "https://randomuser.me/api/portraits/women/52.jpg",
            "https://randomuser.me/api/portraits/women/53.jpg",
            "https://randomuser.me/api/portraits/women/54.jpg",
            "https://randomuser.me/api/portraits/women/55.jpg",
            "https://randomuser.me/api/portraits/women/56.jpg",
            "https://randomuser.me/api/portraits/women/57.jpg",
            "https://randomuser.me/api/portraits/women/58.jpg",
            "https://randomuser.me/api/portraits/women/59.jpg",
            "https://randomuser.me/api/portraits/women/60.jpg",
            "https://randomuser.me/api/portraits/men/58.jpg",
            "https://randomuser.me/api/portraits/men/5.jpg",
    };
}
