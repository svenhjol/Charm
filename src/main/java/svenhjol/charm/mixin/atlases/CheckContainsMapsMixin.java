package svenhjol.charm.mixin.atlases;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.map.MapState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import svenhjol.charm.module.atlases.Atlases;

@Mixin(MapState.class)
public class CheckContainsMapsMixin {

    /**
     * Redirects the normal player inventory contains method to
     * also check for atlases containing maps.
     */
    @Redirect(
        method = "update",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/player/PlayerInventory;contains(Lnet/minecraft/item/ItemStack;)Z"
        )
    )
    private boolean hookContains(PlayerInventory inventory, ItemStack itemStack) {
        return Atlases.inventoryContainsMap(inventory, itemStack);
    }
}
