package svenhjol.charm.base;

import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import svenhjol.charm.Charm;

import java.util.HashMap;
import java.util.Map;

public class CharmSounds {
    public static Map<Identifier, SoundEvent> REGISTER = new HashMap<>();

    public static final SoundEvent BOOKSHELF_OPEN = createSound("bookshelf_open");
    public static final SoundEvent BOOKSHELF_CLOSE = createSound("bookshelf_close");

    public static void init() {
        REGISTER.forEach((id, sound) -> Registry.register(Registry.SOUND_EVENT, id, sound));
    }

    public static SoundEvent createSound(String name) {
        Identifier id = new Identifier(Charm.MOD_ID, name);
        SoundEvent sound = new SoundEvent(id);
        REGISTER.put(id, sound);
        return sound;
    }
}
