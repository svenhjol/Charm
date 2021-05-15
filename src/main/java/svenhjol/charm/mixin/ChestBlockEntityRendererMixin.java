package svenhjol.charm.mixin;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.block.entity.ChestBlockEntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.client.StorageLabelsClient;

@Mixin(ChestBlockEntityRenderer.class)
public abstract class ChestBlockEntityRendererMixin<T extends BlockEntity> {
    @Inject(
        method = "<init>",
        at = @At("TAIL")
    )
    private void hookInit(BlockEntityRendererFactory.Context ctx, CallbackInfo ci) {
        StorageLabelsClient.chestBlockEntityContext.set(ctx);
    }
}
