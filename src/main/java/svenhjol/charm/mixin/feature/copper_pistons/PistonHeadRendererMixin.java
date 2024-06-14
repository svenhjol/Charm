package svenhjol.charm.mixin.feature.copper_pistons;

import com.llamalad7.mixinextras.injector.ModifyReceiver;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.PistonHeadRenderer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.piston.PistonMovingBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.charmony.Resolve;
import svenhjol.charm.feature.copper_pistons.CopperPistons;

@Mixin(PistonHeadRenderer.class)
public class PistonHeadRendererMixin {
    @Unique
    private boolean isCopperPiston = false;

    @Inject(
        method = "render(Lnet/minecraft/world/level/block/piston/PistonMovingBlockEntity;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;II)V",
        at = @At("HEAD")
    )
    private void hookCheckIfCopperPiston(PistonMovingBlockEntity blockEntity, float ticks, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j, CallbackInfo ci) {
        isCopperPiston = blockEntity.getBlockState().is(Resolve.feature(CopperPistons.class).registers.movingCopperPistonBlock.get());
    }

    @ModifyReceiver(
            method = "render(Lnet/minecraft/world/level/block/piston/PistonMovingBlockEntity;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;II)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/Block;defaultBlockState()Lnet/minecraft/world/level/block/state/BlockState;"
            )
    )
    private Block modifyPistonHead(Block originalInstance) {
        Block newInstance = null;

        if (isCopperPistonBlock()) {
            newInstance = Resolve.feature(CopperPistons.class).registers.copperPistonHeadBlock.get();
        }

        return newInstance != null ? newInstance : originalInstance;
    }

    @Unique
    private boolean isCopperPistonBlock() {
        return isCopperPiston;
    }
}
