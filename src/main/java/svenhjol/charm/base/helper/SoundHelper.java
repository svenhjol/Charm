package svenhjol.charm.base.helper;

import com.google.common.collect.Multimap;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.client.sound.SoundSystem;
import net.minecraft.sound.SoundCategory;
import svenhjol.charm.mixin.accessor.SoundManagerAccessor;
import svenhjol.charm.mixin.accessor.SoundSystemAccessor;

public class SoundHelper {
    public static SoundManager getSoundManager() {
        return MinecraftClient.getInstance().getSoundManager();
    }

    public static Multimap<SoundCategory, SoundInstance> getPlayingSounds() {
        SoundSystem soundSystem = ((SoundManagerAccessor) getSoundManager()).getSoundSystem();
        return ((SoundSystemAccessor)soundSystem).getSounds();
    }
}