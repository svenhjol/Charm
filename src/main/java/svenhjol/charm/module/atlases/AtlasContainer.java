package svenhjol.charm.module.atlases;

import svenhjol.charm.helper.ItemNBTHelper;
import svenhjol.charm.module.atlases.AtlasInventory;
import svenhjol.charm.module.atlases.Atlases;
import svenhjol.charm.screen.CharmScreenHandler;
import svenhjol.charm.screen.ConditionalSlot;

import java.util.Objects;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Items;

public class AtlasContainer extends CharmScreenHandler {
    private final svenhjol.charm.module.atlases.AtlasInventory atlasInventory;

    public AtlasContainer(int syncId, Inventory player, svenhjol.charm.module.atlases.AtlasInventory inventory) {
        super(svenhjol.charm.module.atlases.Atlases.CONTAINER, syncId, player, inventory);
        this.atlasInventory = inventory;
        // container's inventory slots
        for (int r = 0; r < 3; ++r) {
            this.addSlot(new ConditionalSlot(stack -> stack.getItem() == Items.MAP, inventory, r, 8, 18 + r * 18));

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
                    return getItem().getItem() != Atlases.ATLAS_ITEM || !Objects.equals(ItemNBTHelper.getUuid(getItem(), svenhjol.charm.module.atlases.AtlasInventory.ID),
                        ItemNBTHelper.getUuid(atlasInventory.getAtlasItem(), svenhjol.charm.module.atlases.AtlasInventory.ID));
                }
            });
        }
    }

    public AtlasInventory getAtlasInventory() {
        return atlasInventory;
    }
}
