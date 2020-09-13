package svenhjol.charm.base;

import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import svenhjol.charm.Charm;

public class CharmSounds {
    public static final SoundEvent BOOKSHELF_OPEN = createSound("bookshelf_open");
    public static final SoundEvent BOOKSHELF_CLOSE = createSound("bookshelf_close");

    public static SoundEvent createSound(String name) {
        Identifier res = new Identifier(Charm.MOD_ID, name);
        SoundEvent sound = new SoundEvent(res);
        Registry.register(Registry.SOUND_EVENT, res, sound);
        return sound;
    }
}
