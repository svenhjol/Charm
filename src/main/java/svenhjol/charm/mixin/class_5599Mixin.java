package svenhjol.charm.mixin;

import net.minecraft.class_5599;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.base.helper.EntityHelper;

@Mixin(class_5599.class)
public class class_5599Mixin {
    /**
     * Looks for custom entity layers before attempting to look for vanilla layers.
     */
    @Inject(
        method = "method_32072",
        at = @At("HEAD"),
        cancellable = true
    )
    public void hookMethod_32072(EntityModelLayer layer, CallbackInfoReturnable<ModelPart> cir) {
        if (EntityHelper.LAYERS.containsKey(layer))
            cir.setReturnValue(EntityHelper.LAYERS.get(layer));
    }
}
