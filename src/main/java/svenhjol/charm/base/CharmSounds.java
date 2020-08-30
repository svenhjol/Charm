package svenhjol.charm.base;

import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import svenhjol.charm.Charm;
import svenhjol.meson.MesonMod;

import java.util.HashMap;
import java.util.Map;

public class CharmSounds {
    private static final Map<Identifier, SoundEvent> REGISTER = new HashMap<>();

    public static final SoundEvent BOOKSHELF_OPEN = createSound("bookshelf_open");
    public static final SoundEvent BOOKSHELF_CLOSE = createSound("bookshelf_close");

    public static SoundEvent createSound(String name) {
        Identifier res = new Identifier(Charm.MOD_ID, name);
        SoundEvent sound = new SoundEvent(res);
        REGISTER.put(res, sound);
        return sound;
    }

    public static void init(MesonMod mod) {
        REGISTER.forEach((res, sound) -> {
            Registry.register(Registry.SOUND_EVENT, res, sound);
        });
    }
}
