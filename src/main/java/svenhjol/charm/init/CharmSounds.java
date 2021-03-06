package svenhjol.charm.init;

import svenhjol.charm.Charm;
import svenhjol.charm.helper.RegistryHelper;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class CharmSounds {
    public static Map<ResourceLocation, SoundEvent> REGISTER = new HashMap<>();

    public static final SoundEvent BOOKSHELF_OPEN = createSound("bookshelf_open");
    public static final SoundEvent BOOKSHELF_CLOSE = createSound("bookshelf_close");
    public static final SoundEvent CASK = createSound("cask");
    public static final SoundEvent COOKING_POT = createSound("cooking_pot");
    public static final SoundEvent QUADRANT = createSound("quadrant");
    public static final SoundEvent RAID_HORN = createSound("raid_horn");

    public static void init() {
        REGISTER.forEach(RegistryHelper::sound);
    }

    public static SoundEvent createSound(String name) {
        ResourceLocation id = new ResourceLocation(Charm.MOD_ID, name);
        SoundEvent sound = new SoundEvent(id);
        REGISTER.put(id, sound);
        return sound;
    }
}
