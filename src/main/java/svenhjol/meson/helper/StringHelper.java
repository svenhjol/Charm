package svenhjol.meson.helper;

import com.google.common.base.CaseFormat;

public class StringHelper {
    public static String snakeToUpperCamel(String string) {
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, string).substring(0, 1).toUpperCase().substring(1);
    }

    public static String upperCamelToSnake(String string) {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, string);
    }
}
