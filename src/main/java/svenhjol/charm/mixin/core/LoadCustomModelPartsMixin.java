package svenhjol.charm.mixin.core;

import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.helper.EntityHelper;
import svenhjol.charm.annotation.CharmMixin;

@Mixin(EntityModelSet.class)
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
    public void hookGetModelPart(ModelLayerLocation layer, CallbackInfoReturnable<ModelPart> cir) {
        if (EntityHelper.LAYERS.containsKey(layer))
            cir.setReturnValue(EntityHelper.LAYERS.get(layer));
    }
}
