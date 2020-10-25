package svenhjol.charm.mixin.accessor;

import net.minecraft.client.sound.MovingSoundInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MovingSoundInstance.class)
public interface MovingSoundInstanceAccessor {
    @Accessor
    void setDone(boolean done);

    @Accessor
    boolean getDone();
}
