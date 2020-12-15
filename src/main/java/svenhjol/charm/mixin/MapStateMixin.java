package svenhjol.charm.mixin;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.map.MapState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import svenhjol.charm.module.Atlas;

@Mixin(MapState.class)
public class MapStateMixin {
    @Redirect(
        method = "update",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/player/PlayerInventory;contains(Lnet/minecraft/item/ItemStack;)Z"
        )
    )
    private boolean hookContains(PlayerInventory inventory, ItemStack itemStack) {
        return Atlas.inventoryContainsMap(inventory, itemStack);
    }
}
