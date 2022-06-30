package svenhjol.charm.helper;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;

public class TextHelper {
    public static Component empty() {
        return TextComponent.EMPTY;
    }
    public static MutableComponent literal(String string) {
        return new TextComponent(string);
    }

    public static MutableComponent translatable(String string) {
        return new TranslatableComponent(string);
    }

    public static MutableComponent translatable(String string, Object ... objects) {
        return new TranslatableComponent(string, objects);
    }
}
