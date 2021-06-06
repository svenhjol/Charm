package svenhjol.charm.mixin.variant_chests;

import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.resources.model.Material;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.properties.ChestType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.module.variant_chests.VariantChestBlockEntityRenderer;

@Mixin(Sheets.class)
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
    private static void hookGetChestTexture(BlockEntity blockEntity, ChestType type, boolean christmas, CallbackInfoReturnable<Material> cir) {
        Material spriteIdentifier = VariantChestBlockEntityRenderer.getMaterial(blockEntity, type);
        if (spriteIdentifier != null)
            cir.setReturnValue(spriteIdentifier);
    }
}