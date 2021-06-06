package svenhjol.charm.mixin.armor_invisibility;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.module.colored_glints.ColoredGlintHandler;
import svenhjol.charm.module.armor_invisibility.ArmorInvisibility;

@Mixin(HumanoidArmorLayer.class)
public class PreventArmorRenderMixin<T extends LivingEntity, M extends HumanoidModel<T>, A extends HumanoidModel<T>> {
    @Inject(
        method = "renderArmorPiece",
        at = @At("HEAD"),
        cancellable = true
    )
    private void hookRenderArmor(PoseStack matrices, MultiBufferSource vertexConsumers, T livingEntity, EquipmentSlot equipmentSlot, int i, A bipedEntityModel, CallbackInfo ci) {
        ItemStack stack = livingEntity.getItemBySlot(equipmentSlot);
        if (ArmorInvisibility.shouldArmorBeInvisible(livingEntity, stack))
            ci.cancel();

        // take a reference to the item being rendered, this is needed for the glint consumer
        ColoredGlintHandler.targetStack = livingEntity.getItemBySlot(equipmentSlot);
    }
}