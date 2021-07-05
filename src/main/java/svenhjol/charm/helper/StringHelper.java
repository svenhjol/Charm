package svenhjol.charm.helper;

import com.google.common.base.CaseFormat;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.metadata.ModMetadata;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * @version 1.0.1-charm
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

    @Nullable
    public static String tryResolveLanguageKey(String modId, String key) {
        if (!languageStrings.containsKey(modId)) {
            // try and parse the mod's en_us language file into a json object
            FabricLoader.getInstance().getModContainer(modId).ifPresent(container -> {
                ModMetadata metadata = container.getMetadata();
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
            if (el != null) {
                return el.getAsString();
            }
            LogHelper.debug(StringHelper.class, "Could not resolve key `" + key + "` for mod `" + modId + "`");
        }

        return null;
    }
}
