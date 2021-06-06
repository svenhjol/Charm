package svenhjol.charm.mixin.accessor;

import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.client.sounds.SoundManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import svenhjol.charm.annotation.CharmMixin;

@Mixin(SoundManager.class)
@CharmMixin(required = true)
public interface SoundManagerAccessor {
    @Accessor()
    SoundEngine getSoundSystem();
}
