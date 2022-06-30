package svenhjol.charm.helper;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
public class TextHelper {
    public static Component empty() {
        return Component.empty();
    }
    public static MutableComponent literal(String string) {
        return Component.literal(string);
    }

    public static MutableComponent translatable(String string) {
        return Component.translatable(string);
    }

    public static MutableComponent translatable(String string, Object ... objects) {
        return Component.translatable(string, objects);
    }
}
