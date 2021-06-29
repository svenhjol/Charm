package svenhjol.charm.mixin.accessor;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerResources;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MinecraftServer.class)
public interface MinecraftServerAccessor {
    @Accessor
    ServerResources getResources();
}
