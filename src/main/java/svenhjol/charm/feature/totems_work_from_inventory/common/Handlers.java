package svenhjol.charm.feature.totems_work_from_inventory.common;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import svenhjol.charm.api.enums.TotemType;
import svenhjol.charm.feature.totems_work_from_inventory.TotemsWorkFromInventory;
import svenhjol.charm.foundation.feature.FeatureHolder;

public final class Handlers extends FeatureHolder<TotemsWorkFromInventory> {
    public Handlers(TotemsWorkFromInventory feature) {
        super(feature);
    }

    public ItemStack tryUsingTotemOfUndying(LivingEntity entity) {
        if (entity instanceof Player player) {
            ItemStack found = null;
            for (var provider : feature().providers.inventoryCheckProviders) {
                var item = provider.findTotemFromInventory(player, TotemType.UNDYING);
                if (item.isPresent()) {
                    found = item.get();
                    break;
                }
            }

            if (found != null) {
                feature().advancements.usedTotemFromInventory(player);
                return found;
            }
        }

        return ItemStack.EMPTY;
    }
}
