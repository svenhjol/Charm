package svenhjol.charm.mixin.accessor;

import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(FeatureRenderer.class)
public interface FeatureRendererAccessor<T extends Entity, M extends EntityModel<T>> {
    @Accessor
    void setContext(FeatureRendererContext<T, M> renderer);
}
