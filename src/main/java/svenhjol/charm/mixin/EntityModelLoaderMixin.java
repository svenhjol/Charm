package svenhjol.charm.mixin;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.base.helper.EntityHelper;

@Mixin(EntityModelLoader.class)
public class EntityModelLoaderMixin {
    /**
     * Looks for custom entity layers before attempting to look for vanilla layers.
     */
    @Inject(
        method = "getModelPart",
        at = @At("HEAD"),
        cancellable = true
    )
    public void hookGetModelPart(EntityModelLayer layer, CallbackInfoReturnable<ModelPart> cir) {
        if (EntityHelper.LAYERS.containsKey(layer))
            cir.setReturnValue(EntityHelper.LAYERS.get(layer));
    }
}
