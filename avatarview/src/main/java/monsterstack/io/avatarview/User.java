package monsterstack.io.avatarview;

public class User {
    private int color;
    private String name;
    private String avatarUrl;

    public User() {}

    public User(String name, String avatarUrl, int color) {
        this.name = name;
        this.avatarUrl = avatarUrl;
        this.color = color;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
