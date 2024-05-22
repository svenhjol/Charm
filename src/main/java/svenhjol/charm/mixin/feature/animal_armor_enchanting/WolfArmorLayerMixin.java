package svenhjol.charm.mixin.feature.animal_armor_enchanting;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.layers.WolfArmorLayer;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.item.AnimalArmorItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import svenhjol.charm.feature.glint_coloring.GlintColoringClient;
import svenhjol.charm.foundation.Resolve;

@Mixin(WolfArmorLayer.class)
public class WolfArmorLayerMixin {
    @Redirect(
        method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/animal/Wolf;FFFFFF)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/MultiBufferSource;getBuffer(Lnet/minecraft/client/renderer/RenderType;)Lcom/mojang/blaze3d/vertex/VertexConsumer;"
        )
    )
    private VertexConsumer hookRender(MultiBufferSource instance, RenderType renderType, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, Wolf wolf) {
        // It's safe to cast to AnimalArmorItem here because it's already been checked further up the render() method.
        var armor = wolf.getBodyArmorItem();
        var item = (AnimalArmorItem)armor.getItem();
        Resolve.feature(GlintColoringClient.class).handlers.setTargetStack(armor);
        return ItemRenderer.getArmorFoilBuffer(multiBufferSource, RenderType.armorCutoutNoCull(item.getTexture()), false);
    }
}