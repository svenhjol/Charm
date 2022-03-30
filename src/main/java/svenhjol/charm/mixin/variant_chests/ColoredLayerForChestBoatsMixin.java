package svenhjol.charm.mixin.variant_chests;

import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.module.variant_chests.VariantChests;

@Mixin(ItemColors.class)
public class ColoredLayerForChestBoatsMixin {
    @Inject(
        method = "createDefault",
        at = @At("RETURN")
    )
    private static void hookCreateDefault(BlockColors blockColors, CallbackInfoReturnable<ItemColors> cir) {
        var itemColors = cir.getReturnValue();
        itemColors.register(((itemStack, i) -> i == 0 ? -1 : VariantChests.getLayerColor(itemStack)), Items.ACACIA_CHEST_BOAT);
    }
}
