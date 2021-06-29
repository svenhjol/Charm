package svenhjol.charm.mixin.accessor;

import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.renderer.RenderBuffers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Minecraft.class)
public interface MinecraftAccessor {
    @Accessor
    RenderBuffers getRenderBuffers();

    @Accessor
    BlockColors getBlockColors();
}
