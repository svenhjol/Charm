package svenhjol.charm.mixin.variant_chests;

import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.world.level.ItemLike;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.module.variant_chests.VariantChests;

@Mixin(ItemColors.class)
public class ColoredLayerForChestBoatsMixin {

    /**
     * Register the chest boat items with the color layer renderer.
     */
    @Inject(
        method = "createDefault",
        at = @At("RETURN")
    )
    private static void hookCreateDefault(BlockColors blockColors, CallbackInfoReturnable<ItemColors> cir) {
        var itemColors = cir.getReturnValue();
        itemColors.register(((itemStack, i) -> i == 0 ? -1 : VariantChests.getLayerColor(itemStack)), VariantChests.CHEST_BOATS.values().toArray(new ItemLike[0]));
    }
}
