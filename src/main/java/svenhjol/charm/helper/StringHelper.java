package svenhjol.charm.helper;

import com.google.common.base.CaseFormat;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public class StringHelper {
    public static String snakeToUpperCamel(String string) {
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, string);
    }

    public static String upperCamelToSnake(String string) {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, string);
    }

    public static String capitalize(String string) {
        if (string == null || string.isEmpty())
            return string;

        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }

    public static String splitOverLines(String string) {
        return splitOverLines(string, 20);
    }

    public static String splitOverLines(String string, int lineLength) {
        StringBuilder out = new StringBuilder();
        int lineSize = 0;

        for (int i = 0; i < string.length(); i++) {
            char currentChar = string.charAt(i);
            if (lineSize++ >= lineLength) {
                if (currentChar == ' ' && string.length() - i > 4) {
                    currentChar = '\n';
                    lineSize = 0;
                }
            }
            out.append(currentChar);
        }
        return out.toString();
    }
}
