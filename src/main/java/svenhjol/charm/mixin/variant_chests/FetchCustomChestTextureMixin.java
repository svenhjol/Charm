package svenhjol.charm.mixin.variant_chests;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.ChestType;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.util.SpriteIdentifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.render.VariantChestBlockEntityRenderer;

@Mixin(TexturedRenderLayers.class)
public class FetchCustomChestTextureMixin {

    /**
     * Defers to {@link VariantChestBlockEntityRenderer#getMaterial} to fetch
     * a custom chest texture according to the provided block entity.
     *
     * If the returned texture is null, use vanilla texture.
     */
    @Inject(
        method = "getChestTexture(Lnet/minecraft/block/entity/BlockEntity;Lnet/minecraft/block/enums/ChestType;Z)Lnet/minecraft/client/util/SpriteIdentifier;",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void hookGetChestTexture(BlockEntity blockEntity, ChestType type, boolean christmas, CallbackInfoReturnable<SpriteIdentifier> cir) {
        SpriteIdentifier spriteIdentifier = VariantChestBlockEntityRenderer.getMaterial(blockEntity, type);
        if (spriteIdentifier != null)
            cir.setReturnValue(spriteIdentifier);
    }
}