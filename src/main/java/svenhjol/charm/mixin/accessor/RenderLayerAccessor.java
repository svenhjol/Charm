package svenhjol.charm.mixin.accessor;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(RenderLayer.class)
public interface RenderLayerAccessor<T extends Entity, M extends EntityModel<T>> {
    @Mutable
    @Accessor
    void setRenderer(RenderLayerParent<T, M> renderer);
}
