package svenhjol.charm.charmony.helper;

import com.google.common.base.CaseFormat;
import com.google.common.base.Function;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public final class TextHelper {
    public static Component empty() {
        return Component.empty();
    }

    public static String snakeToUpperCamel(String string) {
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, string);
    }

    public static String upperCamelToSnake(String string) {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, string);
    }

    public static String snakeToPascal(String string) {
        var ss = string.split("_");
        var b = new StringBuilder();
        for (var s : ss) {
            b.append(capitalize(s));
        }
        return b.toString();
    }

    public static String featureName(String string) {
        var snake = upperCamelToSnake(string);
        var first = snake.substring(0, 1);
        return first + snake.replace("_", " ").substring(1);
    }

    public static String capitalize(String string) {
        if (string == null || string.isEmpty()) {
            return string;
        }

        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }

    public static String uncapitalize(String string) {
        if (string == null || string.isEmpty()) {
            return string;
        }

        return string.substring(0, 1).toLowerCase() + string.substring(1);
    }

    public static String splitOverLines(String string) {
        return splitOverLines(string, 20);
    }

    public static String splitOverLines(String string, int lineLength) {
        var out = new StringBuilder();
        int lineSize = 0;

        for (int i = 0; i < string.length(); i++) {
            var currentChar = string.charAt(i);
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

    public static List<Component> toComponents(String string, int lineLength) {
        List<Component> out = new ArrayList<>();
        int lineSize = 0;
        StringBuilder buffer = new StringBuilder();
        Function<String, Component> convertText =
            s -> Component.literal(s.trim().replace("\n", " "));

        for (int i = 0; i < string.length(); i++) {
            var currentChar = string.charAt(i);
            buffer.append(currentChar);
            if (lineSize++ >= lineLength) {
                if (currentChar == ' ' && string.length() - i > 4) {
                    out.add(convertText.apply(buffer.toString()));
                    buffer = new StringBuilder();
                    lineSize = 0;
                }
            }
        }

        out.add(convertText.apply(buffer.toString()));
        return out;
    }

    /**
     * @link {<a href="https://stackoverflow.com/a/45321638">...</a>}
     */
    public static String createRegexFromGlob(String glob) {
        var out = new StringBuilder();
        for (int i = 0; i < glob.length(); i++) {
            var c = glob.charAt(i);
            switch (c) {
                case '*' -> out.append(".*");
                case '?' -> out.append(".");
                case '.' -> out.append("\\.");
                case '\\' -> out.append("\\\\");
                default -> out.append(c);
            }
        }
        return out.toString();
    }
}
