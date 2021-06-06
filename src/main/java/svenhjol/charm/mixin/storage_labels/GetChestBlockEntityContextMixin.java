package svenhjol.charm.mixin.storage_labels;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.ChestRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.module.storage_labels.StorageLabelsClient;

@Mixin(ChestRenderer.class)
public abstract class GetChestBlockEntityContextMixin {

    /**
     * Fetches the vanilla chest block entity context so that the
     * storage label renderer can use it.
     *
     * Makes no runtime modification to this class.
     */
    @Inject(
        method = "<init>",
        at = @At("TAIL")
    )
    private void hookInit(BlockEntityRendererProvider.Context ctx, CallbackInfo ci) {
        StorageLabelsClient.chestBlockEntityContext.set(ctx);
    }
}
