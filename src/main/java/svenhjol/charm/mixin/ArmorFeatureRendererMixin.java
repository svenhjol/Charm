package svenhjol.charm.mixin;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
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
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.base.handler.ModuleHandler;
import svenhjol.charm.handler.ColoredGlintHandler;
import svenhjol.charm.module.ArmorInvisibility;

@Mixin(ArmorFeatureRenderer.class)
public class ArmorFeatureRendererMixin<T extends LivingEntity, M extends BipedEntityModel<T>, A extends BipedEntityModel<T>> {
    private ItemStack itemStackToRender;

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
        this.itemStackToRender = livingEntity.getEquippedStack(equipmentSlot);
    }

    /**
     * Call Charm's glint handler instead.
     */
    @Redirect(
        method = "renderArmorParts",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/render/item/ItemRenderer;getArmorGlintConsumer(Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/client/render/RenderLayer;ZZ)Lnet/minecraft/client/render/VertexConsumer;"
        )
    )
    private VertexConsumer hookRenderArmorParts(VertexConsumerProvider provider, RenderLayer layer, boolean solid, boolean glint) {
        return ColoredGlintHandler.getArmorGlintConsumer(provider, layer, solid, glint, this.itemStackToRender);
    }
}
