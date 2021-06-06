package svenhjol.charm.mixin.accessor;

import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;
import svenhjol.charm.annotation.CharmMixin;

@Mixin(ServerPlayerEntity.class)
@CharmMixin(required = true)
public interface ServerPlayerEntityAccessor {
    @Mutable
    @Accessor
    void setInTeleportationState(boolean flag);
}
