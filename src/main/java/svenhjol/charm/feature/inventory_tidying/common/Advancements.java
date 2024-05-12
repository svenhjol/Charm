package svenhjol.charm.feature.inventory_tidying.common;

import net.minecraft.world.entity.player.Player;
import svenhjol.charm.feature.inventory_tidying.InventoryTidying;
import svenhjol.charm.foundation.feature.AdvancementHolder;

public final class Advancements extends AdvancementHolder<InventoryTidying> {
    public Advancements(InventoryTidying feature) {
        super(feature);
    }

    public void tidiedInventory(Player player) {
        trigger("tidied_inventory", player);
    }
}
