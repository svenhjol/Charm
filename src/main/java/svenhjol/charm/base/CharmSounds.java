package svenhjol.charm.base;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import svenhjol.charm.Charm;
import svenhjol.meson.MesonInstance;
import svenhjol.meson.handler.RegistryHandler;

import java.util.ArrayList;
import java.util.List;

public class CharmSounds {
    public static List<SoundEvent> soundsToRegister = new ArrayList<>();

    public static final SoundEvent BOOKSHELF_OPEN = createSound("bookshelf_open");
    public static final SoundEvent BOOKSHELF_CLOSE = createSound("bookshelf_close");
    public static final SoundEvent HOMING = createSound("homing");
    public static final SoundEvent WOOD_SMASH = createSound("wood_smash");
    public static final SoundEvent FUMAROLE_BUBBLING = createSound("fumarole_bubbling");
    public static final SoundEvent FUMAROLE_ERUPT = createSound("fumarole_erupt");

    public static SoundEvent createSound(String name) {
        ResourceLocation res = new ResourceLocation(Charm.MOD_ID, name);
        SoundEvent sound = new SoundEvent(res).setRegistryName(res);
        soundsToRegister.add(sound);
        return sound;
    }

    public static void init(MesonInstance instance) {
        soundsToRegister.forEach(RegistryHandler::registerSound);
        instance.log.debug("Registered sounds");
    }
}
