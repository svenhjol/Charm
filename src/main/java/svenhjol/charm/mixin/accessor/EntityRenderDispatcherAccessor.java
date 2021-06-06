package svenhjol.charm.mixin.accessor;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import svenhjol.charm.annotation.CharmMixin;

import java.util.Map;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.world.entity.EntityType;

@Mixin(EntityRenderDispatcher.class)
@CharmMixin(required = true)
public interface EntityRenderDispatcherAccessor {
    @Accessor
    Map<EntityType<?>, EntityRenderer<?>> getRenderers();
}
