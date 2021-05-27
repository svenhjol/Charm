package svenhjol.charm.module.atlases;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.Slot;
import svenhjol.charm.helper.ItemNBTHelper;
import svenhjol.charm.screen.CharmScreenHandler;
import svenhjol.charm.screen.ConditionalSlot;

import java.util.Objects;

public class AtlasContainer extends CharmScreenHandler {
    private final AtlasInventory atlasInventory;

    public AtlasContainer(int syncId, PlayerInventory player, AtlasInventory inventory) {
        super(Atlases.CONTAINER, syncId, player, inventory);
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
                public boolean canTakeItems(PlayerEntity playerIn) {
                    return getStack().getItem() != Atlases.ATLAS_ITEM || !Objects.equals(ItemNBTHelper.getUuid(getStack(), AtlasInventory.ID),
                        ItemNBTHelper.getUuid(atlasInventory.getAtlasItem(), AtlasInventory.ID));
                }
            });
        }
    }

    public AtlasInventory getAtlasInventory() {
        return atlasInventory;
    }
}
