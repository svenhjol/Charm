package svenhjol.charm.mixin.accessor;

import com.google.common.collect.Multimap;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundSystem;
import net.minecraft.sound.SoundCategory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SoundSystem.class)
public interface SoundSystemAccessor {
    @Accessor
    Multimap<SoundCategory, SoundInstance> getSounds();
}
