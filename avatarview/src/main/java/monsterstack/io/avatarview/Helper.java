package monsterstack.io.avatarview;

public class Helper {
    public static String getShortName(String fullName) {
        String[] strings = fullName.split(" ");//no i18n
        String shortName;
        if (strings.length == 1) {
            shortName = strings[0].substring(0, 2);
        } else {
            shortName = strings[0].substring(0, 1) + strings[1].substring(0, 1);
        }
        return shortName.toUpperCase();
    }
}
