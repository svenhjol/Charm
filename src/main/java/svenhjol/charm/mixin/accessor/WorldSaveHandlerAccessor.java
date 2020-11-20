package svenhjol.charm.mixin.accessor;

import net.minecraft.world.WorldSaveHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.io.File;

@Mixin(WorldSaveHandler.class)
public interface WorldSaveHandlerAccessor {
    @Accessor
    File getPlayerDataDir();
}
