package svenhjol.charm.screenhandler;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import svenhjol.charm.base.screenhandler.CharmInventoryScreenHandler;
import svenhjol.charm.module.Atlas;

public class AtlasContainer extends CharmInventoryScreenHandler {

    public AtlasContainer(int syncId, PlayerInventory player) {
        this(syncId, player, new SimpleInventory(AtlasInventory.SIZE));
    }

    public AtlasContainer(int syncId, PlayerInventory player, Inventory inventory) {
        super(2, Atlas::canAtlasInsertItem, Atlas.CONTAINER, syncId, player, inventory);
    }
}
