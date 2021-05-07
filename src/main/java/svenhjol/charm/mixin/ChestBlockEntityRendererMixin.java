package svenhjol.charm.mixin;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.block.entity.ChestBlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.render.LootableContainerLabelRenderer;

@Mixin(ChestBlockEntityRenderer.class)
public class ChestBlockEntityRendererMixin<T extends BlockEntity> {
    private final ThreadLocal<BlockEntityRendererFactory.Context> context = new ThreadLocal<>();

    @Inject(
        method = "<init>",
        at = @At("TAIL")
    )
    private void hookInit(BlockEntityRendererFactory.Context ctx, CallbackInfo ci) {
        this.context.set(ctx);
    }

    @Inject(
        method = "render(Lnet/minecraft/block/entity/BlockEntity;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;II)V",
        at = @At("TAIL")
    )
    private void hookRender(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, CallbackInfo ci) {
        LootableContainerLabelRenderer.render(this.context.get().getRenderDispatcher(), entity, tickDelta, matrices, vertexConsumers, light, overlay);
    }
}
