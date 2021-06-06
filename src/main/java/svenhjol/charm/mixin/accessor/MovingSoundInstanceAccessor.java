package svenhjol.charm.mixin.accessor;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import svenhjol.charm.annotation.CharmMixin;

@Mixin(AbstractTickableSoundInstance.class)
@CharmMixin(required = true)
public interface MovingSoundInstanceAccessor {
    @Accessor
    void setDone(boolean done);

    @Accessor
    boolean getDone();
}
