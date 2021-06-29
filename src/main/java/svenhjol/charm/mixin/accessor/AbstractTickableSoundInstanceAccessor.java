package svenhjol.charm.mixin.accessor;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractTickableSoundInstance.class)
public interface AbstractTickableSoundInstanceAccessor {
    @Accessor
    void setStopped(boolean flag);

    @Accessor
    boolean getStopped();
}
