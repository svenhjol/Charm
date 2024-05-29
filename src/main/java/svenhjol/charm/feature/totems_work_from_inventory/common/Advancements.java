package svenhjol.charm.feature.totems_work_from_inventory.common;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import svenhjol.charm.feature.totems_work_from_inventory.TotemsWorkFromInventory;
import svenhjol.charm.feature.core.custom_advancements.common.AdvancementHolder;

public final class Advancements extends AdvancementHolder<TotemsWorkFromInventory> {
    public Advancements(TotemsWorkFromInventory feature) {
        super(feature);
    }

    public void usedTotemFromInventory(Player player) {
        var mainHand = player.getItemInHand(InteractionHand.MAIN_HAND);
        var offHand = player.getItemInHand(InteractionHand.OFF_HAND);

        if (!mainHand.is(Items.TOTEM_OF_UNDYING) && !offHand.is(Items.TOTEM_OF_UNDYING)) {
            trigger("used_totem_from_inventory", player);
        }
    }
}
