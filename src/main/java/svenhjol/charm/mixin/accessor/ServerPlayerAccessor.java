package svenhjol.charm.mixin.accessor;

import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;
import svenhjol.charm.annotation.CharmMixin;

@Mixin(ServerPlayer.class)
@CharmMixin(required = true)
public interface ServerPlayerAccessor {
    @Mutable
    @Accessor
    void setIsChangingDimension(boolean flag);
}
