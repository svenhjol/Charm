package svenhjol.charm.feature.atlases;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Items;
import svenhjol.charm_core.base.CharmConditionalSlot;
import svenhjol.charm_core.base.CharmContainerMenu;
import svenhjol.charm_core.helper.ItemNbtHelper;

import java.util.Objects;

public class AtlasContainer extends CharmContainerMenu {
    private final AtlasInventory inventory;

    public AtlasContainer(int syncId, Inventory playerInventory) {
        this(syncId, playerInventory, AtlasInventory.find(playerInventory));
    }

    public AtlasContainer(int syncId, Inventory playerInventory, AtlasInventory atlasInventory) {
        super(Atlases.MENU_TYPE.get(), syncId, playerInventory, atlasInventory);
        this.inventory = atlasInventory;

        // Container's inventory slots.
        for (int r = 0; r < 3; ++r) {
            this.addSlot(new CharmConditionalSlot(stack -> stack.getItem() == Items.MAP, atlasInventory, r, 8, 18 + r * 18));
        }

        // Player's main inventory slots.
        for (int r = 0; r < 3; ++r) {
            for (int c = 0; c < 9; ++c) {
                this.addSlot(new Slot(playerInventory, 9 + r * 9 + c, 8 + c * 18, 84 + r * 18));
            }
        }

        // Player's hotbar.
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142) {
                @Override
                public boolean mayPickup(Player playerIn) {
                    return getItem().getItem() != Atlases.ITEM.get() || !Objects.equals(ItemNbtHelper.getUuid(getItem(), AtlasInventory.ID),
                        ItemNbtHelper.getUuid(AtlasContainer.this.inventory.getAtlasItem(), AtlasInventory.ID));
                }
            });
        }
    }

    public AtlasInventory getInventory() {
        return inventory;
    }
}
