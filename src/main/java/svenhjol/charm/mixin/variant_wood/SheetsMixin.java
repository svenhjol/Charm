package svenhjol.charm.mixin.variant_wood;

import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.resources.model.Material;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.properties.ChestType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.feature.variant_wood.renderer.VariantChestBlockEntityRenderer;

/**
 * Defers to {@link VariantChestBlockEntityRenderer#getChestMaterial} to fetch
 * a custom chest texture according to the provided block entity.
 * If the returned texture is null, use vanilla texture.
 */
@Mixin(Sheets.class)
public class SheetsMixin {
    @Inject(
        method = "chooseMaterial(Lnet/minecraft/world/level/block/entity/BlockEntity;Lnet/minecraft/world/level/block/state/properties/ChestType;Z)Lnet/minecraft/client/resources/model/Material;",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void hookGetChestTexture(BlockEntity blockEntity, ChestType type, boolean christmas, CallbackInfoReturnable<Material> cir) {
        var spriteIdentifier = VariantChestBlockEntityRenderer.getChestMaterial(blockEntity, type);

        if (spriteIdentifier != null) {
            cir.setReturnValue(spriteIdentifier);
        }
    }
}