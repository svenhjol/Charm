package svenhjol.charm.mixin.feature.core.custom_pistons;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.renderer.blockentity.PistonHeadRenderer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import svenhjol.charm.charmony.Resolve;
import svenhjol.charm.feature.core.custom_pistons.CustomPistons;

@Mixin(PistonHeadRenderer.class)
public class PistonHeadRendererMixin {

    @WrapOperation(
            method = "render(Lnet/minecraft/world/level/block/piston/PistonMovingBlockEntity;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;II)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z"
            )
    )
    private boolean redirectBlockStateChecks(BlockState instance, Block block, Operation<Boolean> original) {
        if (Resolve.feature(CustomPistons.class).handlers.alsoCheckTags(instance, block)) {
            return true;
        }
        return original.call(instance, block);
    }
}
