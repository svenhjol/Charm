package svenhjol.charm.helper;

import com.google.common.collect.Multimap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.sounds.SoundSource;
import svenhjol.charm.mixin.accessor.SoundManagerAccessor;
import svenhjol.charm.mixin.accessor.SoundEngineAccessor;

public class SoundHelper {
    public static SoundManager getSoundManager() {
        return Minecraft.getInstance().getSoundManager();
    }

    public static Multimap<SoundSource, SoundInstance> getPlayingSounds() {
        SoundEngine soundSystem = ((SoundManagerAccessor) getSoundManager()).getSoundEngine();
        return ((SoundEngineAccessor)soundSystem).getInstanceBySource();
    }
}