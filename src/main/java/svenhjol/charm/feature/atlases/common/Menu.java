package svenhjol.charm.feature.atlases.common;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Items;
import svenhjol.charm.charmony.Resolve;
import svenhjol.charm.charmony.common.menu.CharmConditionalSlot;
import svenhjol.charm.charmony.common.menu.CharmContainerMenu;
import svenhjol.charm.feature.atlases.Atlases;

import java.util.Objects;

public class Menu extends CharmContainerMenu {
    private static final Atlases ATLASES = Resolve.feature(Atlases.class);
    private final AtlasInventory inventory;

    public Menu(int syncId, Inventory playerInventory) {
        this(syncId, playerInventory, AtlasInventory.find(playerInventory));
    }

    public Menu(int syncId, Inventory playerInventory, AtlasInventory atlasInventory) {
        super(ATLASES.registers.menuType.get(), syncId, playerInventory, atlasInventory);
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
                    var itemData = AtlasData.get(getItem());
                    var containerData = AtlasData.get(Menu.this.inventory.getAtlasItem());
                    return getItem().getItem() != ATLASES.registers.item.get()
                        || !Objects.equals(itemData.getId(), containerData.getId());
                }
            });
        }
    }

    public AtlasInventory getInventory() {
        return inventory;
    }
}
