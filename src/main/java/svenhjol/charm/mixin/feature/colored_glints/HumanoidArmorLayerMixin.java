package svenhjol.charm.mixin.feature.colored_glints;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.feature.colored_glints.ColoredGlintsClient;

@Mixin(HumanoidArmorLayer.class)
public class HumanoidArmorLayerMixin<T extends LivingEntity, M extends HumanoidModel<T>, A extends HumanoidModel<T>> {
    /**
     * Fetches the entity's armor so that the colored glint handler can modify its color.
     * Makes no runtime modification to this class.
     */
    @Inject(
        method = "renderArmorPiece",
        at = @At("HEAD")
    )
    private void hookRenderArmor(PoseStack matrices, MultiBufferSource vertexConsumers, T livingEntity, EquipmentSlot equipmentSlot, int i, A bipedEntityModel, CallbackInfo ci) {
        // take a reference to the item being rendered, this is needed for the glint consumer
        ColoredGlintsClient.targetStack = livingEntity.getItemBySlot(equipmentSlot);
    }
}