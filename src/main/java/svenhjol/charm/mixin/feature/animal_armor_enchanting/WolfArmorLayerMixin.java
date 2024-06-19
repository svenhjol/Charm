package svenhjol.charm.mixin.feature.animal_armor_enchanting;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.WolfModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.layers.WolfArmorLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.item.AnimalArmorItem;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.charmony.Resolve;
import svenhjol.charm.feature.glint_coloring.GlintColoringClient;

@Mixin(WolfArmorLayer.class)
public abstract class WolfArmorLayerMixin {
    @Shadow @Final private WolfModel<Wolf> model;
    
    @Inject(
        method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/Entity;FFFFFF)V",
        at = @At("TAIL")
    )
    private void hookRender(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, Entity entity, float f, float g, float h, float j, float k, float l, CallbackInfo ci) {
        maybeRenderGlint(poseStack, multiBufferSource, i, (Wolf)entity);
    }
    
    @Unique
    private void maybeRenderGlint(PoseStack poseStack, MultiBufferSource multiBufferSource, int col, Wolf wolf) {
        var armor = wolf.getBodyArmorItem();
        
        if (armor.isEmpty() || !(armor.getItem() instanceof AnimalArmorItem armorItem)) {
            return;
        }
        
        if (armor.hasFoil()) {
            Resolve.feature(GlintColoringClient.class).handlers.setTargetStack(armor);
            this.model.renderToBuffer(poseStack, multiBufferSource.getBuffer(RenderType.entityGlintDirect()), col, OverlayTexture.NO_OVERLAY);
        }
    }
}