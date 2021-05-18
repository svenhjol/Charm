package svenhjol.charm.mixin.accessor;

import net.minecraft.client.sound.SoundManager;
import net.minecraft.client.sound.SoundSystem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import svenhjol.charm.base.iface.CharmMixin;

@Mixin(SoundManager.class)
@CharmMixin(required = true)
public interface SoundManagerAccessor {
    @Accessor()
    SoundSystem getSoundSystem();
}
