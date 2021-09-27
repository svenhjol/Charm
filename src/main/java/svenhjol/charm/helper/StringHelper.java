package svenhjol.charm.helper;

import com.google.common.base.CaseFormat;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import org.apache.commons.lang3.StringUtils;
import svenhjol.charm.loader.ModuleLoader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * @version 1.0.3-charm
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
public class StringHelper {
    private static final Map<String, JsonObject> languageStrings = new HashMap<>();

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

    public static Optional<String> tryResolveLanguageKey(String modId, String key) {
        if (!languageStrings.containsKey(modId)) {
            // try and parse the mod's en_us language file into a json object
            FabricLoader.getInstance().getModContainer(modId).ifPresent(container -> {
                Path path = container.getPath("assets/" + modId + "/lang/en_us.json");

                try (JsonReader reader = new JsonReader(new InputStreamReader(Files.newInputStream(path), StandardCharsets.UTF_8))) {
                    JsonElement parsed = new JsonParser().parse(reader);
                    languageStrings.put(modId, parsed.getAsJsonObject());
                    LogHelper.debug(StringHelper.class, "Successfully opened language file for `" + modId + "`");
                } catch (IOException e) {
                    LogHelper.error(StringHelper.class, "Failed to open language file: " + e.getMessage());
                }
            });
        }

        if (languageStrings.containsKey(modId)) {
            // try and fetch the resolved language key from the mod's language strings
            JsonElement el = languageStrings.get(modId).get(key);
            if (el != null)
                return Optional.of(el.getAsString());

            LogHelper.debug(StringHelper.class, "Could not resolve key `" + key + "` for mod `" + modId + "`");
        }

        return Optional.empty();
    }

    public static Optional<String> tryTranslateServerComponent(Component message) {
        List<String> modIds = ModuleLoader.getModIds();

        // don't try and work with a non-translatable component
        if (!(message instanceof TranslatableComponent translatable)) return Optional.empty();

        // ignore if it doesn't contain a mod key
        String useModId = "";
        for (String modId : modIds) {
            if (message.getString().contains(modId + "."))
                useModId = modId;
        }
        if (useModId.isEmpty()) return Optional.empty();

        String reduced = message.getString();
        if (reduced.startsWith("[") && reduced.endsWith("]"))
            reduced = reduced.substring(1, reduced.length() - 1);

        try {
            int i = reduced.lastIndexOf("[");
            int j = reduced.lastIndexOf("]");
            String key = i >= 0 && j >= 0 ? reduced.substring(i + 1, j) : reduced;

            if (!key.isEmpty() && key.contains(useModId)) {
                Optional<String> opt = tryResolveLanguageKey(useModId, key);
                if (opt.isPresent()) {
                    LogHelper.debug(StringHelper.class, "Resolved key `" + key + "` to `" + opt.get() + "`");
                    return Optional.of(reduced.replace(key, opt.get()));
                }
            }
        } catch (StringIndexOutOfBoundsException e) {
            LogHelper.debug(StringHelper.class, "Index out of bounds when trying to process `" + reduced + "`: " + e.getMessage());
        }

        return Optional.empty();
    }

    public static OptionalLong parseSeed(String seed) {
        OptionalLong opt1;
        if (StringUtils.isEmpty(seed)) {
            opt1 = OptionalLong.empty();
        } else {
            OptionalLong opt2;
            try {
                opt2 = OptionalLong.of(Long.parseLong(seed));
            } catch (NumberFormatException var4) {
                opt2 = OptionalLong.empty();
            }

            if (opt2.isPresent() && opt2.getAsLong() != 0L) {
                opt1 = opt2;
            } else {
                opt1 = OptionalLong.of(seed.hashCode());
            }
        }

        return opt1;
    }
}
