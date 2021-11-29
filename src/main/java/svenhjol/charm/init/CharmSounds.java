package svenhjol.charm.init;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import svenhjol.charm.Charm;
import svenhjol.charm.registry.CommonRegistry;

import java.util.HashMap;
import java.util.Map;

public class CharmSounds {
    public static Map<ResourceLocation, SoundEvent> REGISTER = new HashMap<>();

    public static void init() {
        REGISTER.forEach(CommonRegistry::sound);
    }

    public static SoundEvent createSound(String name) {
        ResourceLocation id = new ResourceLocation(Charm.MOD_ID, name);
        SoundEvent sound = new SoundEvent(id);
        REGISTER.put(id, sound);
        return sound;
    }
}
