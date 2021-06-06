package svenhjol.charm.mixin.accessor;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import svenhjol.charm.annotation.CharmMixin;

@Mixin(RenderLayer.class)
@CharmMixin(required = true)
public interface FeatureRendererAccessor<T extends Entity, M extends EntityModel<T>> {
    @Accessor
    void setContext(RenderLayerParent<T, M> renderer);
}
