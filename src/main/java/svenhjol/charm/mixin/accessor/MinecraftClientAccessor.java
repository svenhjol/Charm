package svenhjol.charm.mixin.accessor;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.render.BufferBuilderStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import svenhjol.charm.base.iface.CharmMixin;

@Mixin(MinecraftClient.class)
@CharmMixin(required = true)
public interface MinecraftClientAccessor {
    @Accessor
    BufferBuilderStorage getBufferBuilders();

    @Accessor
    BlockColors getBlockColors();
}
