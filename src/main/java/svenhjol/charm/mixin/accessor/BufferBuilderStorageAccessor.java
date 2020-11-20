package svenhjol.charm.mixin.accessor;

import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferBuilderStorage;
import net.minecraft.client.render.RenderLayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.SortedMap;

@Mixin(BufferBuilderStorage.class)
public interface BufferBuilderStorageAccessor {
    @Accessor
    SortedMap<RenderLayer, BufferBuilder> getEntityBuilders();
}
