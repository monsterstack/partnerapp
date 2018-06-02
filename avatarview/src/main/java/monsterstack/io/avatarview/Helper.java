package monsterstack.io.avatarview;

public class Helper {
    private static final String EMPTY_STRING = "";

    public static String getShortName(User user) {
        String shortName;

        if (user instanceof EmptyUser) {
            shortName = EMPTY_STRING;
        } else {

            String[] strings = user.getName().split(" ");//no i18n
            if (strings.length == 1) {
                shortName = strings[0].substring(0, 2);
            } else {
                shortName = strings[0].substring(0, 1) + strings[1].substring(0, 1);
            }
        }
        return shortName.toUpperCase();
    }
}
