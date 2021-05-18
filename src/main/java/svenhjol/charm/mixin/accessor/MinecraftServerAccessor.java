package svenhjol.charm.mixin.accessor;

import net.minecraft.resource.ServerResourceManager;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import svenhjol.charm.base.iface.CharmMixin;

@Mixin(MinecraftServer.class)
@CharmMixin(required = true)
public interface MinecraftServerAccessor {
    @Accessor
    ServerResourceManager getServerResourceManager();
}
