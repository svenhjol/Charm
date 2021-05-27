package svenhjol.charm.mixin.colored_glints;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.ElytraFeatureRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.module.colored_glints.ColoredGlintHandler;

@Mixin(ElytraFeatureRenderer.class)
public class GetElytraMixin<T extends LivingEntity> {

    /**
     * Fetches the entity's elytra so that the colored glint handler can modify its color.
     * Makes no runtime modification to this class.
     */
    @Inject(
        method = "render",
        at = @At("HEAD")
    )
    private void hookRender(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, T livingEntity, float f, float g, float h, float j, float k, float l, CallbackInfo ci) {
        ColoredGlintHandler.targetStack = livingEntity.getEquippedStack(EquipmentSlot.CHEST);
    }
}
