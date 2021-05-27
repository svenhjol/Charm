package svenhjol.charm.mixin.accessor;

import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import svenhjol.charm.annotation.CharmMixin;

import java.util.Map;

@Mixin(EntityRenderDispatcher.class)
@CharmMixin(required = true)
public interface EntityRenderDispatcherAccessor {
    @Accessor
    Map<EntityType<?>, EntityRenderer<?>> getRenderers();
}
