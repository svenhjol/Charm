package svenhjol.charm.mixin.accessor;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerResources;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import svenhjol.charm.annotation.CharmMixin;

@Mixin(MinecraftServer.class)
@CharmMixin(required = true)
public interface MinecraftServerAccessor {
    @Accessor
    ServerResources getServerResourceManager();
}
