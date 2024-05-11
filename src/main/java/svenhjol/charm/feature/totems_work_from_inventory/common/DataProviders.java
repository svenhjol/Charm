package svenhjol.charm.feature.totems_work_from_inventory.common;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import svenhjol.charm.api.enums.TotemType;
import svenhjol.charm.api.iface.ITotemInventoryCheckProvider;
import svenhjol.charm.feature.totems_work_from_inventory.TotemsWorkFromInventory;
import svenhjol.charm.foundation.feature.FeatureHolder;

import java.util.Optional;

public final class DataProviders extends FeatureHolder<TotemsWorkFromInventory> implements ITotemInventoryCheckProvider {
    public DataProviders(TotemsWorkFromInventory feature) {
        super(feature);
    }

    @Override
    public Optional<ItemStack> findTotemFromInventory(Player player, TotemType totemType) {
        if (totemType == TotemType.UNDYING) {
            var mainHand = player.getMainHandItem();
            if (mainHand.is(Items.TOTEM_OF_UNDYING)) {
                return Optional.of(mainHand);
            }

            var offHand = player.getOffhandItem();
            if (offHand.is(Items.TOTEM_OF_UNDYING)) {
                return Optional.of(offHand);
            }

            for (var item : player.getInventory().items) {
                if (item.is(Items.TOTEM_OF_UNDYING)) {
                    return Optional.of(item);
                }
            }
        }

        return Optional.empty();
    }
}
