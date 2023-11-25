package svenhjol.charm.mixin.enchantable_horse_armor;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.layers.HorseArmorLayer;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.item.HorseArmorItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import svenhjol.charmony.feature.colored_glints.ColoredGlintsClient;

@Mixin(HorseArmorLayer.class)
public class HorseArmorLayerMixin {
    @Redirect(
        method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/animal/horse/Horse;FFFFFF)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/MultiBufferSource;getBuffer(Lnet/minecraft/client/renderer/RenderType;)Lcom/mojang/blaze3d/vertex/VertexConsumer;"
        )
    )
    private VertexConsumer hookRender(MultiBufferSource instance, RenderType renderType, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, Horse horse) {
        var armor = horse.getArmor();
        var item = (HorseArmorItem)armor.getItem();
        ColoredGlintsClient.targetStack = armor;
        return ItemRenderer.getArmorFoilBuffer(multiBufferSource, RenderType.armorCutoutNoCull(item.getTexture()), false, armor.hasFoil());
    }
}