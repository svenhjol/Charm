package svenhjol.charm.mixin.accessor;

import com.google.common.collect.Multimap;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundSystem;
import net.minecraft.sound.SoundCategory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import svenhjol.charm.annotation.CharmMixin;

@Mixin(SoundSystem.class)
@CharmMixin(required = true)
public interface SoundSystemAccessor {
    @Accessor
    Multimap<SoundCategory, SoundInstance> getSounds();
}
