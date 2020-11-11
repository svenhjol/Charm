package svenhjol.charm.mixin;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.base.handler.ModuleHandler;
import svenhjol.charm.handler.ColoredGlintHandler;
import svenhjol.charm.module.ArmorInvisibility;

@Mixin(ArmorFeatureRenderer.class)
public class ArmorFeatureRendererMixin<T extends LivingEntity, M extends BipedEntityModel<T>, A extends BipedEntityModel<T>> {
    @Inject(
        method = "renderArmor",
        at = @At("HEAD"),
        cancellable = true
    )
    private void hookRenderArmor(MatrixStack matrices, VertexConsumerProvider vertexConsumers, T livingEntity, EquipmentSlot equipmentSlot, int i, A bipedEntityModel, CallbackInfo ci) {
        if (ModuleHandler.enabled("charm:armor_invisibility")) {
            ItemStack stack = livingEntity.getEquippedStack(equipmentSlot);
            if (ArmorInvisibility.shouldArmorBeInvisible(livingEntity, stack))
                ci.cancel();
        }

        // take a reference to the item being rendered, this is needed for the glint consumer
        ColoredGlintHandler.targetStack = livingEntity.getEquippedStack(equipmentSlot);
    }
}
