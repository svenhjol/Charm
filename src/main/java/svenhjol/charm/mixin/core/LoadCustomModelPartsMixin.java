package svenhjol.charm.mixin.core;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.helper.EntityHelper;
import svenhjol.charm.annotation.CharmMixin;

@Mixin(EntityModelLoader.class)
@CharmMixin(required = true)
public class LoadCustomModelPartsMixin {

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
