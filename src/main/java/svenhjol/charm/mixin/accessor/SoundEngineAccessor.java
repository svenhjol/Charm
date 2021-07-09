package svenhjol.charm.mixin.accessor;

import com.google.common.collect.Multimap;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.sounds.SoundSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SoundEngine.class)
public interface SoundEngineAccessor {
    @Accessor
    Multimap<SoundSource, SoundInstance> getInstanceBySource();
}
