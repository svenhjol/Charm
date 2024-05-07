package svenhjol.charm.feature.totems_work_from_inventory;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import svenhjol.charm.api.enums.TotemType;

public final class CommonCallbacks {

    public static ItemStack tryUsingTotemOfUndying(LivingEntity entity) {
        if (entity instanceof Player player) {
            ItemStack found = null;
            for (var provider : TotemsWorkFromInventory.inventoryCheckProviders) {
                var item = provider.findTotemFromInventory(player, TotemType.UNDYING);
                if (item.isPresent()) {
                    found = item.get();
                    break;
                }
            }

            if (found != null) {
                TotemsWorkFromInventory.triggerUsedTotemOfUndyingFromInventory(player);
                return found;
            }
        }

        return ItemStack.EMPTY;
    }
}
