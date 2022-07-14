package svenhjol.charm.module.atlases;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Items;
import svenhjol.charm.helper.ItemNbtHelper;
import svenhjol.charm.menu.CharmConditionalSlot;
import svenhjol.charm.menu.CharmContainerMenu;

import java.util.Objects;

public class AtlasContainer extends CharmContainerMenu {
    private final AtlasInventory atlasInventory;

    public AtlasContainer(int syncId, Inventory player, AtlasInventory inventory) {
        super(Atlases.MENU, syncId, player, inventory);
        this.atlasInventory = inventory;
        // container's inventory slots
        for (int r = 0; r < 3; ++r) {
            this.addSlot(new CharmConditionalSlot(stack -> stack.getItem() == Items.MAP, inventory, r, 8, 18 + r * 18));

        }

        // player's main inventory slots
        for (int r = 0; r < 3; ++r) {
            for (int c = 0; c < 9; ++c) {
                this.addSlot(new Slot(player, 9 + r * 9 + c, 8 + c * 18, 84 + r * 18));
            }
        }

        // player's hotbar
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(player, i, 8 + i * 18, 142) {
                @Override
                public boolean mayPickup(Player playerIn) {
                    return getItem().getItem() != Atlases.ATLAS_ITEM || !Objects.equals(ItemNbtHelper.getUuid(getItem(), AtlasInventory.ID),
                        ItemNbtHelper.getUuid(atlasInventory.getAtlasItem(), AtlasInventory.ID));
                }
            });
        }
    }

    public AtlasInventory getAtlasInventory() {
        return atlasInventory;
    }
}
